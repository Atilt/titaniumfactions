package com.massivecraft.factions.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import me.lucko.helper.reflect.MinecraftVersions;
import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Simple Bukkit ScoreBoard API with 1.7 to 1.15 support.
 * Everything is at packet level so you don't need to use it in the main server thread.
 * <p>
 * You can find the project on <a href="https://github.com/MrMicky-FR/FastBoard">GitHub</a>
 *
 * @author MrMicky
 */
public class FastBoard {

    // Packets sending
    private static final Field PLAYER_CONNECTION;

    // Chat components
    private static final Class<?> CHAT_COMPONENT_CLASS;
    private static final Method MESSAGE_FROM_STRING;

    // Scoreboard packets
    private static final Constructor<?> PACKET_SB_OBJ;
    private static final Constructor<?> PACKET_SB_DISPLAY_OBJ;
    private static final Constructor<?> PACKET_SB_SCORE;
    private static final Constructor<?> PACKET_SB_TEAM;

    // Scoreboard enums
    private static final Class<?> ENUM_SB_HEALTH_DISPLAY;
    private static final Class<?> ENUM_SB_ACTION;
    private static final Object ENUM_SB_HEALTH_DISPLAY_INTEGER;
    private static final Object ENUM_SB_ACTION_CHANGE;
    private static final Object ENUM_SB_ACTION_REMOVE;

    private static final Function<Player, Object> PLAYER_TO_NMS;
    private static final BiConsumer<Object, Object> SEND_PACKET = null;

    static {
        try {
            Class<?> craftChatMessageClass = ServerReflection.obcClass("util.CraftChatMessage");
            Class<?> entityPlayerClass = ServerReflection.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = ServerReflection.nmsClass("PlayerConnection");
            Class<?> craftPlayerClass = ServerReflection.obcClass("entity.CraftPlayer");
            Class<?> packetClass = ServerReflection.nmsClass("Packet");

            MESSAGE_FROM_STRING = craftChatMessageClass.getDeclaredMethod("fromString", String.class);
            CHAT_COMPONENT_CLASS = ServerReflection.nmsClass("IChatBaseComponent");

            PLAYER_CONNECTION = entityPlayerClass.getDeclaredField("playerConnection");

            PACKET_SB_OBJ = ServerReflection.nmsClass("PacketPlayOutScoreboardObjective").getConstructor();
            PACKET_SB_DISPLAY_OBJ = ServerReflection.nmsClass("PacketPlayOutScoreboardDisplayObjective").getConstructor();
            PACKET_SB_SCORE = ServerReflection.nmsClass("PacketPlayOutScoreboardScore").getConstructor();
            PACKET_SB_TEAM = ServerReflection.nmsClass("PacketPlayOutScoreboardTeam").getConstructor();

            if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_8)) {
                ENUM_SB_HEALTH_DISPLAY = ServerReflection.nmsClass("IScoreboardCriteria$EnumScoreboardHealthDisplay");

                if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
                    ENUM_SB_ACTION = ServerReflection.nmsClass("ScoreboardServer$Action");
                } else {
                    ENUM_SB_ACTION = ServerReflection.nmsClass("PacketPlayOutScoreboardScore$EnumScoreboardAction");
                }
                ENUM_SB_HEALTH_DISPLAY_INTEGER = Enum.valueOf((Class<Enum>) ENUM_SB_HEALTH_DISPLAY, "INTEGER");
                ENUM_SB_ACTION_CHANGE = Enum.valueOf((Class<Enum>) ENUM_SB_ACTION, "CHANGE");
                ENUM_SB_ACTION_REMOVE = Enum.valueOf((Class<Enum>) ENUM_SB_ACTION, "REMOVE");
            } else {
                ENUM_SB_HEALTH_DISPLAY = null;
                ENUM_SB_ACTION = null;

                ENUM_SB_HEALTH_DISPLAY_INTEGER = null;
                ENUM_SB_ACTION_CHANGE = null;
                ENUM_SB_ACTION_REMOVE = null;
            }
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            PLAYER_TO_NMS = (Function<Player, Object>) LambdaMetafactory.metafactory(lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(entityPlayerClass)),
                    MethodType.methodType(entityPlayerClass, craftPlayerClass)).getTarget().invokeExact();

            //playerConnection -> sendPacket
/*            SEND_PACKET = (BiConsumer<Object, Object>) LambdaMetafactory.metafactory(lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(playerConnectionClass, "sendPacket", MethodType.methodType(packetClass)),
                    MethodType.methodType(packetClass)).getTarget().invoke();*/

        } catch (Throwable exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private final UUID uuid;
    private final String id;

    private String title = ChatColor.RESET.toString();
    private ObjectList<String> lines = new ObjectArrayList<>();

    private boolean deleted = false;

    /**
     * Creates a new FastBoard.
     *
     * @param uuid the player the scoreboard is for
     */
    public FastBoard(UUID uuid) {
        this.uuid = Objects.requireNonNull(uuid, "uuid");
        this.id = "fb-" + Double.toString(Math.random()).substring(2, 10);

        try {
            sendObjectivePacket(ObjectiveMode.CREATE);
            sendDisplayObjectivePacket();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the scoreboard title.
     *
     * @return the scoreboard title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Update the scoreboard title.
     *
     * @param title the new scoreboard title
     * @throws IllegalArgumentException if the title is longer than 32 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    public void updateTitle(String title) {
        if (this.title.equals(Objects.requireNonNull(title, "title"))) {
            return;
        }

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13) && title.length() > 32) {
            throw new IllegalArgumentException("Title is longer than 32 chars");
        }

        this.title = title;

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTitle(FPlayer fPlayer) {
        String attempt = fPlayer.getScoreboardTextProvider().getTitle(fPlayer);
        if (this.title.equals(Objects.requireNonNull(attempt, "title"))) {
            return;
        }

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13) && title.length() > 32) {
            throw new IllegalArgumentException("Title is longer than 32 chars");
        }

        this.title = attempt;

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the scoreboard lines.
     *
     * @return the scoreboard lines
     */
    public List<String> getLines() {
        return ObjectLists.unmodifiable(this.lines);
    }

    /**
     * Get the specified scoreboard line.
     *
     * @param line the line number
     * @return the line
     * @throws IndexOutOfBoundsException if the line is higher than {@code size}
     */
    public String getLine(int line) {
        checkLineNumber(line, true);

        return lines.get(line);
    }

    /**
     * Update a single scoreboard line.
     *
     * @param line the line number
     * @param text the new line text
     * @throws IndexOutOfBoundsException if the line is higher than {@code size} + 1
     */
    public void updateLine(int line, String text) {
        checkLineNumber(line, false);

        try {
            if (line < size()) {
                this.lines.set(line, text);

                sendTeamPacket(getScoreByLine(line), TeamMode.UPDATE);
                return;
            }

            ObjectList<String> newLines = new ObjectArrayList<>(this.lines);
            int total = this.size();

            if (line > total) {
                for (int i = total; i < line; i++) {
                    newLines.add("");
                }
            }

            newLines.add(text);

            updateLines(newLines);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove a scoreboard line.
     *
     * @param line the line number
     */
    public void removeLine(int line) {
        checkLineNumber(line, false);

        if (line >= size()) {
            return; // The line don't exists
        }

        ObjectList<String> lines = new ObjectArrayList<>(this.lines);
        lines.remove(line);
        updateLines(lines);
    }

    /**
     * Update all the scoreboard lines.
     *
     * @param lines the new lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    public void updateLines(String... lines) {
        updateLines(Arrays.asList(lines));
    }

    /**
     * Update the lines of the scoreboard
     *
     * @param lines the new scoreboard lines
     * @throws IllegalArgumentException if one line is longer than 30 chars on 1.12 or lower
     * @throws IllegalStateException    if {@link #delete()} was call before
     */
    public void updateLines(Collection<String> lines) {
        Objects.requireNonNull(lines, "lines");

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13)) {
            int lineCount = 0;
            for (String s : lines) {
                if (s != null && s.length() > 30) {
                    throw new IllegalArgumentException("Line " + lineCount + " is longer than 30 chars");
                }
                lineCount++;
            }
        }

        List<String> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        int linesSize = this.lines.size();

        try {
            if (oldLines.size() != linesSize) {
                List<String> oldLinesCopy = new ArrayList<>(oldLines);

                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);

                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);

                        oldLines.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < linesSize; i++) {
                        sendScorePacket(i, ScoreboardAction.CHANGE);

                        sendTeamPacket(i, TeamMode.CREATE);

                        oldLines.add(oldLines.size() - i, getLineByScore(i));
                    }
                }
            }

            for (int i = 0; i < linesSize; i++) {
                if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
                    sendTeamPacket(i, TeamMode.UPDATE);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLines(FPlayer fPlayer) {
        List<String> lines = fPlayer.getScoreboardTextProvider().getText(fPlayer);
        Objects.requireNonNull(lines, "lines");

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13)) {
            int lineCount = 0;
            for (String s : lines) {
                if (s != null && s.length() > 30) {
                    throw new IllegalArgumentException("Line " + lineCount + " is longer than 30 chars");
                }
                lineCount++;
            }
        }

        List<String> oldLines = new ArrayList<>(this.lines);
        this.lines.clear();
        this.lines.addAll(lines);

        int linesSize = this.lines.size();

        try {
            if (oldLines.size() != linesSize) {
                List<String> oldLinesCopy = new ArrayList<>(oldLines);

                if (oldLines.size() > linesSize) {
                    for (int i = oldLinesCopy.size(); i > linesSize; i--) {
                        sendTeamPacket(i - 1, TeamMode.REMOVE);

                        sendScorePacket(i - 1, ScoreboardAction.REMOVE);

                        oldLines.remove(0);
                    }
                } else {
                    for (int i = oldLinesCopy.size(); i < linesSize; i++) {
                        sendScorePacket(i, ScoreboardAction.CHANGE);

                        sendTeamPacket(i, TeamMode.CREATE);

                        oldLines.add(oldLines.size() - i, getLineByScore(i));
                    }
                }
            }

            for (int i = 0; i < linesSize; i++) {
                if (!Objects.equals(getLineByScore(oldLines, i), getLineByScore(i))) {
                    sendTeamPacket(i, TeamMode.UPDATE);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the player who has the scoreboard.
     *
     * @return current player for this FastBoard
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Get the scoreboard id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Get if the scoreboard is deleted.
     *
     * @return true if the scoreboard is deleted
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Get the scoreboard size (the number of lines).
     *
     * @return the size
     */
    public int size() {
        return lines.size();
    }

    /**
     * Delete this FastBoard, and will remove the scoreboard for the associated player if he is online.
     * After this, all uses of {@link #updateLines} and {@link #updateTitle} will throws an {@link IllegalStateException}
     *
     * @throws IllegalStateException if this was already call before
     */
    public void delete() {
        try {
            for (int i = 0; i < lines.size(); i++) {
                sendTeamPacket(i, TeamMode.REMOVE);
            }

            sendObjectivePacket(ObjectiveMode.REMOVE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        deleted = true;
    }

    private void checkLineNumber(int line, boolean checkMax) {
        if (line < 0) {
            throw new IllegalArgumentException("Line number must be positive");
        }

        if (checkMax && line >= lines.size()) {
            throw new IllegalArgumentException("Line number must be under " + lines.size());
        }
    }

    private int getScoreByLine(int line) {
        return lines.size() - line - 1;
    }

    private String getLineByScore(int score) {
        return getLineByScore(lines, score);
    }

    private String getLineByScore(List<String> lines, int score) {
        return lines.get(lines.size() - score - 1);
    }

    private void sendObjectivePacket(ObjectiveMode mode) throws ReflectiveOperationException {
        Object packet = PACKET_SB_OBJ.newInstance();

        setField(packet, String.class, id);
        setField(packet, int.class, mode.ordinal());

        if (mode != ObjectiveMode.REMOVE) {
            setComponentField(packet, title, 1);

            if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_8)) {
                setField(packet, ENUM_SB_HEALTH_DISPLAY, ENUM_SB_HEALTH_DISPLAY_INTEGER);
            }
        }
        sendPacket(packet);
    }

    private void sendDisplayObjectivePacket() throws ReflectiveOperationException {
        Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

        setField(packet, int.class, 1);
        setField(packet, String.class, id);

        sendPacket(packet);
    }

    private void sendScorePacket(int score, ScoreboardAction action) throws ReflectiveOperationException {
        Object packet = PACKET_SB_SCORE.newInstance();

        setField(packet, String.class, getColorCode(score), 0);

        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_8)) {
            setField(packet, ENUM_SB_ACTION, action == ScoreboardAction.REMOVE ? ENUM_SB_ACTION_REMOVE : ENUM_SB_ACTION_CHANGE);
        }

        if (action == ScoreboardAction.CHANGE) {
            setField(packet, String.class, id, 1);
            setField(packet, int.class, score);
        }

        sendPacket(packet);
    }

    private void sendTeamPacket(int score, TeamMode mode) throws ReflectiveOperationException {
        if (mode == TeamMode.ADD_PLAYERS || mode == TeamMode.REMOVE_PLAYERS) {
            throw new UnsupportedOperationException();
        }

        Object packet = PACKET_SB_TEAM.newInstance();

        setField(packet, String.class, id + ':' + score); // Team name
        setField(packet, int.class, mode.ordinal(), 1); // Update mode

        if (mode == TeamMode.CREATE || mode == TeamMode.UPDATE) {
            String line = getLineByScore(score);
            String prefix;
            String suffix = null;

            if (line == null || line.isEmpty()) {
                prefix = getColorCode(score) + ChatColor.RESET;
            } else if (line.length() <= 16 || FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
                prefix = line;
            } else {
                // Prevent splitting color codes
                int index = line.charAt(15) == ChatColor.COLOR_CHAR ? 15 : 16;
                prefix = line.substring(0, index);
                String suffixTmp = line.substring(index);
                ChatColor chatColor = null;

                if (suffixTmp.length() >= 2 && suffixTmp.charAt(0) == ChatColor.COLOR_CHAR) {
                    chatColor = ChatColor.getByChar(suffixTmp.charAt(1));
                }

                String color = ChatColor.getLastColors(prefix);
                boolean addColor = chatColor == null || chatColor.isFormat();

                suffix = (addColor ? (color.isEmpty() ? ChatColor.RESET : color) : "") + suffixTmp;
            }

            if (FactionsPlugin.getInstance().getMCVersion() != MinecraftVersions.v1_13) {
                if (prefix.length() > 16 || (suffix != null && suffix.length() > 16)) {
                    // Something went wrong, just cut to prevent client crash/kick
                    prefix = prefix.substring(0, 16);
                    suffix = (suffix != null) ? suffix.substring(0, 16) : null;
                }
            }

            setComponentField(packet, prefix, 2); // Prefix
            setComponentField(packet, suffix == null ? "" : suffix, 3); // Suffix
            setField(packet, String.class, "always", 4); // Visibility for 1.8+
            setField(packet, String.class, "always", 5); // Collisions for 1.9+

            if (mode == TeamMode.CREATE) {
                setField(packet, Collection.class, Collections.singletonList(getColorCode(score))); // Players in the team
            }
        }

        sendPacket(packet);
    }

    private String getColorCode(int score) {
        return TextUtil.BUKKIT_COLORS[score].toString();
    }

    private void sendPacket(Object packet) throws ReflectiveOperationException {
        Player player = Bukkit.getPlayer(this.uuid);
        if (player == null || !player.isOnline()) {
            return;
        }
        Object entityPlayer = PLAYER_TO_NMS.apply(player);
        Object playerConnection = PLAYER_CONNECTION.get(entityPlayer);
        Method method = playerConnection.getClass().getDeclaredMethod("sendPacket", ServerReflection.nmsClass("Packet"));
        method.invoke(playerConnection, packet);

        ///SEND_PACKET.accept(playerConnection, packet);
    }

    private void setField(Object object, Class<?> fieldType, Object value) throws ReflectiveOperationException {
        setField(object, fieldType, value, 0);
    }

    private void setField(Object object, Class<?> fieldType, Object value, int count) throws ReflectiveOperationException {
        int i = 0;

        for (Field f : object.getClass().getDeclaredFields()) {
            if (f.getType() == fieldType && i++ == count) {
                f.setAccessible(true);
                f.set(object, value);
            }
        }
    }

    private void setComponentField(Object object, String value, int count) throws ReflectiveOperationException {
        if (FactionsPlugin.getInstance().getMCVersion() != MinecraftVersions.v1_13) {
            setField(object, String.class, value, count);
            return;
        }

        int i = 0;
        for (Field f : object.getClass().getDeclaredFields()) {
            if ((f.getType() == String.class || f.getType() == CHAT_COMPONENT_CLASS) && i++ == count) {
                f.setAccessible(true);
                f.set(object, Array.get(MESSAGE_FROM_STRING.invoke(null, value), 0));
            }
        }
    }

    enum ObjectiveMode {
        CREATE, REMOVE, UPDATE
    }

    enum TeamMode {
        CREATE,
        REMOVE,
        UPDATE,
        ADD_PLAYERS,
        REMOVE_PLAYERS

    }

    enum ScoreboardAction {

        CHANGE,
        REMOVE

    }
}
