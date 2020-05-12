package com.massivecraft.factions.util;

import com.massivecraft.factions.protocol.Protocol;
import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * With help from https://www.spigotmc.org/threads/send-titles-to-players-using-spigot-1-8-1-11-2.48819/
 */
public final class TitleAPI {

    private static boolean SUPPORTED = false;

    private static Method CHAT_TITLE;
    private static Constructor<?> PACKET_TITLE;
    private static Field TITLE;
    private static Field SUBTITLE;

    private TitleAPI() {
        try {
            Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
            SUPPORTED = true;
        } catch (NoSuchMethodException e) {
            try {
                Class<?> chatComponent = ServerReflection.nmsClass("IChatBaseComponent");

                CHAT_TITLE = chatComponent.getDeclaredClasses()[0].getMethod("a", String.class);
                PACKET_TITLE = ServerReflection.nmsClass("PacketPlayOutTitle").getConstructor(ServerReflection.nmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], chatComponent, int.class, int.class, int.class);
                TITLE = ServerReflection.nmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE");
                SUBTITLE = ServerReflection.nmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE");
            } catch (NoSuchMethodException | NoSuchFieldException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Send a title to player
     *
     * @param player      Player to send the title to
     * @param title       The text displayed in the title
     * @param subtitle    The text displayed in the subtitle
     * @param fadeInTime  The time the title takes to fade in
     * @param showTime    The time the title is displayed
     * @param fadeOutTime The time the title takes to fade out
     */
    public static void send(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        if (SUPPORTED) {
            player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
            return;
        }
        try {
            Object chatTitle = CHAT_TITLE.invoke(null, "{\"text\": \"" + title + "\"}");
            Object chatsubTitle = CHAT_TITLE.invoke(null, "{\"text\": \"" + subtitle + "\"}");

            Object titlePacket = PACKET_TITLE.newInstance(TITLE.get(null), chatTitle, fadeInTime, showTime, fadeOutTime);
            Object subTitlePacket = PACKET_TITLE.newInstance(SUBTITLE.get(null), chatsubTitle, fadeInTime, showTime, fadeOutTime);

            Protocol.sendPacket(player, titlePacket, subTitlePacket);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
