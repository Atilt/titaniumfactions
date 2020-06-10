package com.massivecraft.factions.meta.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.protocol.Protocol;
import com.massivecraft.factions.util.TextUtil;
import me.lucko.helper.reflect.MinecraftVersions;
import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Simple Bukkit ScoreBoard API with 1.7 to 1.15 support.
 * Everything is at packet level so you don't need to use it in the main server thread.
 * <p>
 * You can find the project on <a href="https://github.com/MrMicky-FR/FastBoard">GitHub</a>
 *
 * @author MrMicky
 */
public class FastBoard {

    // Chat components
    public static final Class<?> CHAT_COMPONENT_CLASS;
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

    static {
        try {
            Class<?> craftChatMessageClass = ServerReflection.obcClass("util.CraftChatMessage");

            MESSAGE_FROM_STRING = craftChatMessageClass.getDeclaredMethod("fromString", String.class);
            CHAT_COMPONENT_CLASS = ServerReflection.nmsClass("IChatBaseComponent");

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

        } catch (Throwable exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    private final Player player;
    private final String id;

    private String title = ChatColor.RESET.toString();
    private List<String> lines = new ArrayList<>();

    private boolean deleted = false;

    /**
     * Creates a new FastBoard.
     *
     * @param player the player the scoreboard is for
     */
    public FastBoard(Player player) {
        this.player = Objects.requireNonNull(player, "player");
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
            this.title = title.substring(0, 33);
        } else {
            this.title = title;
        }

        try {
            sendObjectivePacket(ObjectiveMode.UPDATE);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTitle(FPlayer fPlayer) {
        String attempt = FactionsPlugin.getInstance().isClipPlaceholderAPIHooked() ? FactionsPlugin.getInstance().getClipPlaceholderAPIManager().setPlaceholders(this.player, fPlayer.getScoreboardTextProvider().getTitle(fPlayer)) : fPlayer.getScoreboardTextProvider().getTitle(fPlayer);
        if (this.title.equals(Objects.requireNonNull(attempt, "title"))) {
            return;
        }

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13) && attempt.length() > 32) {
            this.title = attempt.substring(0, 33);
        } else {
            this.title = attempt;
        }

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
        return Collections.unmodifiableList(this.lines);
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

            List<String> newLines = new ArrayList<>(this.lines);
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

        List<String> lines = new ArrayList<>(this.lines);
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
    public void updateLines(List<String> lines) {
        Objects.requireNonNull(lines, "lines");

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13)) {

            ListIterator<String> possible = lines.listIterator();
            while (possible.hasNext()) {
                String current = possible.next();
                if (current.length() > 30) {
                    possible.set(current.substring(0, 31));
                }
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
        List<String> lines = FactionsPlugin.getInstance().isClipPlaceholderAPIHooked() ? FactionsPlugin.getInstance().getClipPlaceholderAPIManager().setPlaceholders(this.player, fPlayer.getScoreboardTextProvider().getText(fPlayer)) : fPlayer.getScoreboardTextProvider().getText(fPlayer);
        Objects.requireNonNull(lines, "lines");

        if (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13)) {
            ListIterator<String> possible = lines.listIterator();
            while (possible.hasNext()) {
                String current = possible.next();
                if (current.length() > 30) {
                    possible.set(current.substring(0, 31));
                }
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
    public Player getPlayer() {
        return this.player;
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
        Protocol.sendPacket(this.player, packet);
    }

    private void sendDisplayObjectivePacket() throws ReflectiveOperationException {
        Object packet = PACKET_SB_DISPLAY_OBJ.newInstance();

        setField(packet, int.class, 1);
        setField(packet, String.class, id);

        Protocol.sendPacket(this.player, packet);
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

        Protocol.sendPacket(this.player, packet);
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
        Protocol.sendPacket(this.player, packet);
    }

    private String getColorCode(int score) {
        return TextUtil.BUKKIT_COLORS[score].toString();
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
