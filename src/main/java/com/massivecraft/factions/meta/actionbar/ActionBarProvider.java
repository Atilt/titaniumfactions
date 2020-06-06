package com.massivecraft.factions.meta.actionbar;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.FastBoard;
import com.massivecraft.factions.protocol.Protocol;
import com.massivecraft.factions.util.TitleAPI;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.lucko.helper.reflect.MinecraftVersions;
import me.lucko.helper.reflect.ServerReflection;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.UUID;
import java.util.function.BiFunction;

public final class ActionBarProvider {

    private static final boolean ACTION_BAR_SUPPORTED = FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_12);

    private static BiFunction<Object, Object, Object> PACKET_INSTANCE;

    static {
        if (!ACTION_BAR_SUPPORTED) {
            try {
                Class<?> packetChat = ServerReflection.nmsClass("PacketPlayOutChat");

                MethodHandles.Lookup lookup = MethodHandles.lookup();
                MethodHandle handle = lookup.findConstructor(packetChat, MethodType.methodType(void.class, FastBoard.CHAT_COMPONENT_CLASS, byte.class));

                PACKET_INSTANCE = (BiFunction<Object, Object, Object>) LambdaMetafactory.metafactory(lookup,
                        "apply",
                        MethodType.methodType(BiFunction.class),
                        handle.type().generic(),
                        handle,
                        handle.type())
                        .getTarget().invokeExact();
            } catch (Throwable exception) {
                exception.printStackTrace();
            }
        }
    }

    private final Object2IntMap<UUID> tasks = new Object2IntOpenHashMap<>();

    private static ActionBarProvider instance;

    ActionBarProvider() {}

    public static ActionBarProvider get() {
        if (instance == null) {
            synchronized (ActionBarProvider.class) {
                if (instance == null) {
                    instance = new ActionBarProvider();
                    instance.tasks.defaultReturnValue(-10);
                }
            }
        }
        return instance;
    }

    public void send(Player player, String message, long ticks) {
        int task = this.tasks.getInt(player.getUniqueId());

        if (task != this.tasks.defaultReturnValue()) {
            Bukkit.getScheduler().cancelTask(task);
        }

        this.tasks.put(player.getUniqueId(), new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                boolean running = this.count < ticks;
                if (ACTION_BAR_SUPPORTED) {
                    sendProvisional(player, running ? message : "");
                } else {
                    sendLegacy(player, running ? message : "");
                }
                if (!running) {
                    this.cancel();
                    tasks.removeInt(player.getUniqueId());
                    return;
                }
                this.count++;
            }
        }.runTaskTimer(FactionsPlugin.getInstance(), 0L, 1L).getTaskId());
    }

    private void sendProvisional(Player player, String message) {
        player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    private void sendLegacy(Player player, String message) {
        Protocol.sendPacket(player, PACKET_INSTANCE.apply(TitleAPI.STRING_TO_COMPONENT.apply("{\"text\": \"" + message + "\"}"), (byte) 2));
    }

    public void untrack(Player player) {
        int task = this.tasks.removeInt(player.getUniqueId());
        if (task != this.tasks.defaultReturnValue()) {
            Bukkit.getScheduler().cancelTask(task);
        }
    }
}
