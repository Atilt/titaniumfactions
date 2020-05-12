package com.massivecraft.factions.protocol;

import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.entity.Player;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class Protocol {

    private static Function<Player, Object> PLAYER_TO_NMS;
    private static BiConsumer<Object, Object> SEND_PACKET;

    private static Field PLAYER_CONNECTION;

    static {
        try {
            Class<?> entityPlayerClass = ServerReflection.nmsClass("EntityPlayer");
            Class<?> playerConnectionClass = ServerReflection.nmsClass("PlayerConnection");
            Class<?> craftPlayerClass = ServerReflection.obcClass("entity.CraftPlayer");
            Class<?> packetClass = ServerReflection.nmsClass("Packet");

            MethodHandles.Lookup lookup = MethodHandles.lookup();

            PLAYER_TO_NMS = (Function<Player, Object>) LambdaMetafactory.metafactory(lookup,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(craftPlayerClass, "getHandle", MethodType.methodType(entityPlayerClass)),
                    MethodType.methodType(entityPlayerClass, craftPlayerClass)).getTarget().invokeExact();


            //playerConnection -> sendPacket
            SEND_PACKET = (BiConsumer<Object, Object>) LambdaMetafactory.metafactory(lookup,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(Object.class, Object.class),
                    lookup.findVirtual(playerConnectionClass, "sendPacket", MethodType.methodType(void.class, packetClass)),
                    MethodType.methodType(packetClass)).getTarget().invoke();

            PLAYER_CONNECTION = entityPlayerClass.getDeclaredField("playerConnection");
            PLAYER_CONNECTION.setAccessible(true);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Protocol() {
        throw new UnsupportedOperationException("this class cannot be instantiated");
    }

    public static void sendPacket(Player player, Object packet) {
        Object entityPlayer = PLAYER_TO_NMS.apply(player);
        try {
            SEND_PACKET.accept(PLAYER_CONNECTION.get(entityPlayer), packet);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object... packets) {
        Object entityPlayer = PLAYER_TO_NMS.apply(player);
        try {
            for (Object packet : packets) {
                SEND_PACKET.accept(PLAYER_CONNECTION.get(entityPlayer), packet);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}