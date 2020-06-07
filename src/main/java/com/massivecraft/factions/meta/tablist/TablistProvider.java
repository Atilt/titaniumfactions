package com.massivecraft.factions.meta.tablist;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.protocol.Protocol;
import com.massivecraft.factions.util.TitleProvider;
import me.lucko.helper.bucket.Bucket;
import me.lucko.helper.bucket.factory.BucketFactory;
import me.lucko.helper.bucket.partitioning.PartitioningStrategies;
import me.lucko.helper.reflect.MinecraftVersions;
import me.lucko.helper.reflect.ServerReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public final class TablistProvider implements AutoCloseable {
    
    private static TablistProvider instance;

    private int task = -1;
    private final Bucket<Player> players = BucketFactory.newHashSetBucket(20, PartitioningStrategies.lowestSize());

    private static final boolean TAB_SUPPORTED = FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_15);

    private static Field HEADER_FIELD;
    private static Field FOOTER_FIELD;
    private static Supplier<Object> PACKET_INSTANCE;

    static {
        if (!TAB_SUPPORTED) {
            try {
                Class<?> packetHeaderFooter = ServerReflection.nmsClass("PacketPlayOutPlayerListHeaderFooter");

                HEADER_FIELD = packetHeaderFooter.getDeclaredField("a");
                FOOTER_FIELD = packetHeaderFooter.getDeclaredField("b");

                HEADER_FIELD.setAccessible(true);
                FOOTER_FIELD.setAccessible(true);

                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle handle = lookup.findConstructor(packetHeaderFooter, MethodType.methodType(void.class));

                PACKET_INSTANCE = (Supplier<Object>) LambdaMetafactory.metafactory(lookup,
                        "get",
                        MethodType.methodType(Supplier.class),
                        handle.type().generic(),
                        handle,
                        handle.type())
                        .getTarget().invokeExact();
            } catch (Throwable exception) {
                exception.printStackTrace();
            }
        }
    }


    TablistProvider() {}

    public static TablistProvider get() {
        if (instance == null) {
            synchronized (TablistProvider.class) {
                if (instance == null) {
                    instance = new TablistProvider();
                }
            }
        }
        return instance;
    }

    public void start() {
        this.task = Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), () -> {
            if (!FactionsPlugin.getInstance().conf().tablist().isEnabled()) {
                return;
            }
            for (Player player : this.players.asCycle().next()) {
                this.send(player, FactionsPlugin.getInstance().conf().tablist().getHeaderColored(), FactionsPlugin.getInstance().conf().tablist().getFooterColored());
            }
        }, 1L, 1L).getTaskId();
    }

    private Object constructPacket(String header, String footer) {
        Object packet = PACKET_INSTANCE.get();
        try {
            HEADER_FIELD.set(packet, TitleProvider.STRING_TO_COMPONENT.apply("{\"text\": \"" + header + "\"}"));
            FOOTER_FIELD.set(packet, TitleProvider.STRING_TO_COMPONENT.apply("{\"text\": \"" + footer + "\"}"));
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return packet;
    }

    public void send(Player player, String header, String footer) {
        if (TAB_SUPPORTED) {
            player.setPlayerListHeaderFooter(header, footer);
            return;
        }
        Protocol.sendPacket(player, this.constructPacket(header, footer));
    }

    public boolean track(Player player) {
        return this.players.add(player);
    }

    public void trackAll() {
        boolean enabled = FactionsPlugin.getInstance().conf().tablist().isEnabled();
        if (!enabled && !this.players.isEmpty()) {
            this.players.clear();
            Object packet = this.constructPacket("", "");
            for (Player player : Bukkit.getOnlinePlayers()) {
                Protocol.sendPacket(player, packet);
            }
        }
    }

    public boolean untrack(Player player) {
        return this.players.remove(player);
    }

    @Override
    public void close() {
        if (this.task != -1) {
            Bukkit.getScheduler().cancelTask(this.task);
        }
    }
}
