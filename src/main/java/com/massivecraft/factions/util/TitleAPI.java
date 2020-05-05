package com.massivecraft.factions.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * With help from https://www.spigotmc.org/threads/send-titles-to-players-using-spigot-1-8-1-11-2.48819/
 */
public final class TitleAPI {

    private static TitleAPI instance;

    public static TitleAPI get() {
        if (instance == null) {
            synchronized (TitleAPI.class) {
                if (instance == null) {
                    instance = new TitleAPI();
                }
            }
        }
        return instance;
    }

    private boolean api = false;

    private Method methodChatTitle;
    private Method methodGetHandle;
    private Method methodSendPacket;
    private Constructor<?> titleConstructor;
    private Field fieldTitle;
    private Field fieldSubTitle;
    private Field fieldPlayerConnection;

    private Class<?> packetClazz = getNMSClass("Packet");

    private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private TitleAPI() {
        try {
            Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
            api = true;
        } catch (NoSuchMethodException e) {
            try {
                this.methodChatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class);
                this.titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
                this.fieldTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE");
                this.fieldSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE");
            } catch (NoSuchMethodException | NoSuchFieldException exception) {
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
    public void sendTitle(Player player, String title, String subtitle, int fadeInTime, int showTime, int fadeOutTime) {
        if (this.api) {
            player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
            return;
        }
        if (methodChatTitle == null) {
            return;
        }
        try {
            Object chatTitle = methodChatTitle.invoke(null, "{\"text\": \"" + title + "\"}");
            Object chatsubTitle = methodChatTitle.invoke(null, "{\"text\": \"" + subtitle + "\"}");

            Object titlePacket = titleConstructor.newInstance(fieldTitle.get(null), chatTitle, fadeInTime, showTime, fadeOutTime);
            Object subTitlePacket = titleConstructor.newInstance(fieldSubTitle.get(null), chatsubTitle, fadeInTime, showTime, fadeOutTime);

            sendPacket(player, titlePacket);
            sendPacket(player, subTitlePacket);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }
    }

    private void sendPacket(Player player, Object packet) {
        try {
            if (this.methodGetHandle == null) {
                this.methodGetHandle = player.getClass().getMethod("getHandle");
            }
            Object handle = this.methodGetHandle.invoke(player);
            if (this.fieldPlayerConnection == null) {
                this.fieldPlayerConnection = handle.getClass().getField("playerConnection");
            }
            Object playerConnection = this.fieldPlayerConnection.get(handle);
            if (this.methodSendPacket == null) {
                this.methodSendPacket = playerConnection.getClass().getMethod("sendPacket", packetClazz);
            }
            this.methodSendPacket.invoke(playerConnection, packet);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get NMS class using reflection
     *
     * @param name Name of the class
     * @return Class
     */
    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + VERSION + "." + name);
        } catch (ClassNotFoundException exception) {
            return null;
        }
    }
}
