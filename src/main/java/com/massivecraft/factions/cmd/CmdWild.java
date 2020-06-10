package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cooldown.Cooldown;
import com.massivecraft.factions.cooldown.WildCooldown;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.wild.WildWorld;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.material.MaterialDb;
import io.papermc.lib.PaperLib;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CmdWild extends FCommand {

    private final Map<UUID, Cooldown> cooldowns = new HashMap<>();
    private final Object2IntMap<UUID> delays = new Object2IntOpenHashMap<>();

    private static final Set<Biome> OCEANS = EnumSet.noneOf(Biome.class);

    static {
        OCEANS.add(Biome.OCEAN);
        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_13)) {
            OCEANS.add(Biome.COLD_OCEAN);
            OCEANS.add(Biome.DEEP_COLD_OCEAN);
            OCEANS.add(Biome.DEEP_FROZEN_OCEAN);
            OCEANS.add(Biome.DEEP_LUKEWARM_OCEAN);
            OCEANS.add(Biome.DEEP_OCEAN);
            OCEANS.add(Biome.DEEP_WARM_OCEAN);
            OCEANS.add(Biome.FROZEN_OCEAN);
            OCEANS.add(Biome.LUKEWARM_OCEAN);
            OCEANS.add(Biome.WARM_OCEAN);
        }
    }

    public CmdWild() {
        super();

        this.aliases.add("wilderness");
        this.aliases.add("wild");
        this.aliases.add("w");

        this.requirements = new CommandRequirements.Builder(Permission.WILD)
                .playerOnly()
                .build();

        this.delays.defaultReturnValue(-1);
    }

    @Override
    public void perform(CommandContext context) {
        World world = context.player.getWorld();
        if (!FactionsPlugin.getInstance().getWildManager().hasSupport(world)) {
            context.msg(TL.COMMAND_WILD_WORLD_NO_SUPPORT);
            return;
        }
        if (this.delays.containsKey(context.player.getUniqueId())) {
            context.msg(TL.COMMAND_WILD_ALREADY_TELEPORTING);
            return;
        }
        Cooldown cooldown = this.cooldowns.getOrDefault(context.player.getUniqueId(), Cooldown.EMPTY);
        if (cooldown.isFinished()) {
            LocationFinder locationFinder = new LocationFinder(FactionsPlugin.getInstance().getWildManager().getWildWorld(world));
            Location location = locationFinder.find(world);
            if (location == null) {
                context.msg(TL.COMMAND_WILD_NO_LOCATION_FOUND);
                return;
            }
            context.msg(TL.COMMAND_WILD_TELEPORT_COMMENCING.format(Long.toString(FactionsPlugin.getInstance().getWildManager().getTeleportDelay())));
            this.cooldowns.put(context.player.getUniqueId(), new WildCooldown(Instant.now(), Duration.ofSeconds(10)));
            this.delays.put(context.player.getUniqueId(), Bukkit.getScheduler().runTaskLater(FactionsPlugin.getInstance(), () -> {
                PaperLib.teleportAsync(context.player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                this.delays.removeInt(context.player.getUniqueId());
            }, 60L).getTaskId());
            return;
        }
        context.msg(cooldown.getRemainingReadable());
    }

    public static class LocationFinder {
        private final WildWorld wildWorld;
        private int tries;

        public LocationFinder(WildWorld wildWorld) {
            this.wildWorld = wildWorld;
        }

        public Location find(World world) {
            if (this.tries == 5) {
                return null;
            }
            Block found = world.getHighestBlockAt(this.wildWorld.selectX(), this.wildWorld.selectZ());
            if (OCEANS.contains(found.getBiome()) || MaterialDb.get().getProvider().isUnsafe(found.getType())) {
                this.tries++;
                return find(world);
            }
            return found.getLocation();
        }
    }
    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_WILD_DESCRIPTION;
    }

    public void untrack(UUID uuid) {
        int task = this.delays.removeInt(uuid);
        if (task != -1) {
            Bukkit.getScheduler().cancelTask(task);
        }
        this.cooldowns.remove(uuid);
    }
}
