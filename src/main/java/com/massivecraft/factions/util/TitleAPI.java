package com.massivecraft.factions.util;

import com.massivecraft.factions.protocol.Protocol;
import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.entity.Player;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public final class TitleAPI {

    private static boolean SUPPORTED = false;

    public static Function<String, Object> STRING_TO_COMPONENT;
    private static Constructor<?> PACKET_TITLE;
    private static Field TITLE;
    private static Field SUBTITLE;

    static  {
        try {
            Player.class.getMethod("sendTitle", String.class, String.class, int.class, int.class, int.class);
            SUPPORTED = true;
        } catch (NoSuchMethodException e) {
            try {
                Class<?> chatComponent = ServerReflection.nmsClass("IChatBaseComponent");
                Class<?> chatSerializer = chatComponent.getDeclaredClasses()[0];
                Class<?> title = ServerReflection.nmsClass("PacketPlayOutTitle");
                Class<?> nested = title.getDeclaredClasses()[0];

                MethodHandles.Lookup lookup = MethodHandles.lookup();

                STRING_TO_COMPONENT = (Function<String, Object>) LambdaMetafactory.metafactory(lookup,
                        "apply",
                        MethodType.methodType(Function.class),
                        MethodType.methodType(Object.class, Object.class),
                        lookup.findStatic(chatSerializer, "a", MethodType.methodType(chatComponent, String.class)),
                        MethodType.methodType(chatSerializer, String.class)).getTarget().invokeExact();

                PACKET_TITLE = title.getConstructor(nested, chatComponent, int.class, int.class, int.class);
                TITLE = nested.getField("TITLE");
                SUBTITLE = nested.getField("SUBTITLE");
            } catch (Throwable exception) {
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
            Object chatTitle = STRING_TO_COMPONENT.apply("{\"text\": \"" + title + "\"}");
            Object chatsubTitle = STRING_TO_COMPONENT.apply("{\"text\": \"" + subtitle + "\"}");

            Object titlePacket = PACKET_TITLE.newInstance(TITLE.get(null), chatTitle, fadeInTime, showTime, fadeOutTime);
            Object subTitlePacket = PACKET_TITLE.newInstance(SUBTITLE.get(null), chatsubTitle, fadeInTime, showTime, fadeOutTime);

            Protocol.sendPacket(player, titlePacket, subTitlePacket);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException exception) {
            exception.printStackTrace();
        }
    }
}
