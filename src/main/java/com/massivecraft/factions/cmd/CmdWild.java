package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cooldown.Cooldown;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.wild.WildManager;
import com.massivecraft.factions.struct.wild.WildWorld;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WorldUtil;
import com.massivecraft.factions.util.material.MaterialDb;
import io.papermc.lib.PaperLib;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.EnumSet;
import java.util.Set;

public class CmdWild extends FCommand {

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
    }

    @Override
    public void perform(CommandContext context) {
        WildManager wildManager = FactionsPlugin.getInstance().getWildManager();
        World world = context.player.getWorld();
        if (!wildManager.hasSupport(world)) {
            context.msg(TL.COMMAND_WILD_WORLD_NO_SUPPORT);
            return;
        }
        if (wildManager.hasDelay(context.player.getUniqueId())) {
            context.msg(TL.COMMAND_WILD_ALREADY_TELEPORTING);
            return;
        }
        Cooldown cooldown = wildManager.getCooldown(context.player.getUniqueId());
        if (cooldown.isFinished()) {
            LocationFinder locationFinder = new LocationFinder(FactionsPlugin.getInstance().getWildManager().getWildWorld(world));
            Location location = locationFinder.find(world);
            if (location == null) {
                context.msg(TL.COMMAND_WILD_NO_LOCATION_FOUND);
                return;
            }
            context.msg(TL.COMMAND_WILD_TELEPORT_COMMENCING.format(wildManager.getTeleportDelayReadable()));
            wildManager.markCooldown(context.player.getUniqueId());
            wildManager.markDelay(context.player.getUniqueId(), Bukkit.getScheduler().runTaskLater(FactionsPlugin.getInstance(), () -> {
                PaperLib.teleportAsync(context.player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
                wildManager.purgeDelay(context.player.getUniqueId());
            }, 60L).getTaskId());
            return;
        }
        context.msg(TL.COMMAND_WILD_ON_COOLDOWN.format(cooldown.getRemainingReadable()));
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
            int x = this.wildWorld.selectX();
            int z = this.wildWorld.selectZ();
            Block found = world.getHighestBlockAt(x, z);
            if (OCEANS.contains(found.getBiome()) || MaterialDb.get().getProvider().isUnsafe(found.getType()) || Board.getInstance().getFactionAt(FLocation.wrap(world.getName(), WorldUtil.blockToChunk(x), WorldUtil.blockToChunk(z))).isNormal()) {
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
}
