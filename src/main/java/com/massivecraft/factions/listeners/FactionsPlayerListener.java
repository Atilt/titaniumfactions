package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.gui.GUI;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.meta.actionbar.ActionBarProvider;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.meta.tablist.TablistProvider;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tasks.FlightTask;
import com.massivecraft.factions.tasks.SeeChunkTask;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.WorldUtil;
import com.massivecraft.factions.util.material.MaterialDb;
import io.papermc.lib.PaperLib;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class FactionsPlayerListener extends AbstractListener {

    private static final Set<EntityType> INTERACTABLE_ENTITIES = EnumSet.noneOf(EntityType.class);

    static {
        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_11)) {
            INTERACTABLE_ENTITIES.add(EntityType.LLAMA);
        }
        if (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_14)) {
            INTERACTABLE_ENTITIES.add(EntityType.TRADER_LLAMA);
        }
        INTERACTABLE_ENTITIES.add(EntityType.ITEM_FRAME);
        INTERACTABLE_ENTITIES.add(EntityType.HORSE);
        INTERACTABLE_ENTITIES.add(EntityType.PIG);
        INTERACTABLE_ENTITIES.add(EntityType.LEASH_HITCH);
        INTERACTABLE_ENTITIES.add(EntityType.MINECART_CHEST);
        INTERACTABLE_ENTITIES.add(EntityType.MINECART_FURNACE);
        INTERACTABLE_ENTITIES.add(EntityType.MINECART_HOPPER);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Bukkit.getOnlineMode() && !FactionsPlugin.getInstance().isFinishedLoading()) {
            Bukkit.getScheduler().runTaskLater(FactionsPlugin.getInstance(), () -> event.getPlayer().kickPlayer(TL.FACTIONS_DATA_LOADING.toString()), 2L);
            return;
        }
        initPlayer(event.getPlayer());
    }

    private void initPlayer(Player player) {
        // Make sure that all online players do have a fplayer.
        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        me.setName(player.getName());

        FactionsPlugin.getInstance().getLandRaidControl().onJoin(me);
        // Update the lastLoginTime for this fplayer
        me.setLastLoginTime(Instant.now().toEpochMilli());

        // Store player's current FLocation and notify them where they are
        me.setLastStoodAt(FLocation.wrap(player.getLocation()));

        me.login(); // set kills / deaths

        if (me.isSpyingChat() && !player.hasPermission(Permission.CHATSPY.node)) {
            me.setSpyingChat(false);
        }

        if (me.isAdminBypassing() && !player.hasPermission(Permission.BYPASS.node)) {
            me.setIsAdminBypassing(false);
        }

        if (WorldUtil.isEnabled(player.getWorld())) {
            this.initFactionWorld(me, player);
        }
        FPlayers.getInstance().addOnline(me);
        FlightTask.get().track(me);
    }

    private void initFactionWorld(FPlayer me, Player player) {
        // Check for Faction announcements. Let's delay this so they actually see it.
        if (me.hasFaction()) {
            Faction faction = me.getFaction();
            if (faction.hasUnreadAnnouncements(me)) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnline() && faction == me.getFaction()) {
                            faction.sendUnreadAnnouncements(player);
                        }
                    }
                }.runTaskLater(FactionsPlugin.getInstance(), 33L); // Due to essentials being an MOTD hog.
            }
            for (FPlayer other : faction.getFPlayersWhereOnline(true)) {
                if (other != me && other.isMonitoringJoins()) {
                    other.msg(TL.FACTION_LOGIN, me.getName());
                }
            }
        }

        if (FactionsPlugin.getInstance().conf().scoreboard().constant().isEnabled() && me.showScoreboard()) {
            me.setTextProvider(SidebarProvider.DEFAULT_SIDEBAR);
            me.setShowScoreboard(true);
        }

        if (FactionsPlugin.getInstance().conf().tablist().isEnabled()) {
            TablistProvider.get().track(player);
        }

        // If they have the permission, don't let them autoleave. Bad inverted setter :\
        me.setAutoLeave(!player.hasPermission(Permission.AUTO_LEAVE_BYPASS.node));
        me.setTakeFallDamage(true);
        if (FactionsPlugin.getInstance().conf().commands().fly().isEnable() && me.isFlying()) { // TODO allow flight to continue
            me.setFlying(false);
        }

        if (SeeChunkTask.get().isStarted() && me.isSeeingChunk()) {
            SeeChunkTask.get().track(me);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        FactionsPlugin.getInstance().getLandRaidControl().onQuit(me);
        // and update their last login time to point to when the logged off, for auto-remove routine
        me.setLastLoginTime(Instant.now().toEpochMilli());

        me.logout(); // cache kills / deaths

        if (me.hasFaction()) {
            Faction myFaction = me.getFaction();

            myFaction.memberLoggedOff();

            if (FactionsPlugin.getInstance().conf().factions().other().isSpawnTeleportIfEnemyTerritory() && myFaction.getRelationTo(Board.getInstance().getFactionAt(me.getLastStoodAt())).isEnemy()) {
                PaperLib.teleportAsync(player, player.getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
            }

            for (FPlayer other : myFaction.getFPlayersWhereOnline(true)) {
                if (other != me && other.isMonitoringJoins()) {
                    other.msg(TL.FACTION_LOGOUT, me.getName());
                }
            }
        }
        FactionsPlugin.getInstance().getStuckSessions().remove(me.getId());
        FactionsPlugin.getInstance().getWildManager().untrack(player);
        FPlayers.getInstance().removeOnline(me);
        FlightTask.get().untrack(me);
        SidebarProvider.get().untrack(me);
        TablistProvider.get().untrack(player);
        ActionBarProvider.get().untrack(player);
        SeeChunkTask.get().untrack(me);
        SeeChunkTask.get().removeBlocks(me, false);
        this.interactSpammers.remove(player.getName());
    }

    // Holds the next time a player can have a map shown.
    private final Object2LongMap<UUID> showTimes = new Object2LongOpenHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }
        if (!FactionsPlugin.getInstance().isFinishedLoading()) {
            //plugin not yet finished loading player & faction data
            event.setCancelled(true);
            return;
        }

        Player player = event.getPlayer();
        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        // clear visualization
        if (FastMath.floor(event.getFrom().getX()) != FastMath.floor(event.getTo().getX()) || FastMath.floor(event.getFrom().getY()) != FastMath.floor(event.getTo().getY()) || FastMath.floor(event.getFrom().getZ()) != FastMath.floor(event.getTo().getZ())) {
            FactionsPlugin.getInstance().getBlockVisualizer().clear(event.getPlayer(), true);
            if (me.isWarmingUp()) {
                me.clearWarmup();
                me.msg(TL.WARMUPS_CANCELLED);
            }
        }

        // quick check to make sure player is moving between chunks; good performance boost
        if (FastMath.floor(event.getFrom().getX()) >> 4 == FastMath.floor(event.getTo().getX()) >> 4 && FastMath.floor(event.getFrom().getZ()) >> 4 == event.getTo().getBlockZ() >> 4 && event.getFrom().getWorld() == event.getTo().getWorld()) {
            return;
        }

        // Did we change coord?
        FLocation from = me.getLastStoodAt();

        if (from.is(event.getTo())) {
            return;
        }

        // Yes we did change coord (:
        FLocation to = FLocation.wrap(event.getTo());
        me.setLastStoodAt(to);

        if (me.getAutoClaimFor() != null) {
            me.attemptClaim(me.getAutoClaimFor(), event.getTo(), true);
        } else if (me.isAutoSafeClaimEnabled()) {
            if (!Permission.MANAGE_SAFE_ZONE.has(player)) {
                me.setIsAutoSafeClaimEnabled(false);
            } else {
                if (!Board.getInstance().getFactionAt(to).isSafeZone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getSafeZone(), to);
                    me.msg(TL.PLAYER_SAFEAUTO);
                }
            }
        } else if (me.isAutoWarClaimEnabled()) {
            if (!Permission.MANAGE_WAR_ZONE.has(player)) {
                me.setIsAutoWarClaimEnabled(false);
            } else {
                if (!Board.getInstance().getFactionAt(to).isWarZone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getWarZone(), to);
                    me.msg(TL.PLAYER_WARAUTO);
                }
            }
        }

        // Did we change "host"(faction)?
        Faction factionFrom = Board.getInstance().getFactionAt(from);
        Faction factionTo = Board.getInstance().getFactionAt(to);
        boolean changedFaction = (factionFrom != factionTo);

        if (FactionsPlugin.getInstance().conf().commands().fly().isEnable() && changedFaction && !me.isAdminBypassing()) {
            boolean canFly = me.canFlyAtLocation();
            if (me.isFlying() && !canFly) {
                me.setFlying(false);
            } else if (me.isAutoFlying() && !me.isFlying() && canFly) {
                me.setFlying(true);
            }
        }

        if (me.isMapAutoUpdating()) {
            if (!showTimes.containsKey(player.getUniqueId()) || (showTimes.getLong(player.getUniqueId()) < Instant.now().toEpochMilli())) {
                me.sendFancyMessage(Board.getInstance().getMap(me, to, player.getLocation().getYaw()));
                showTimes.put(player.getUniqueId(), Instant.now().toEpochMilli() + FactionsPlugin.getInstance().conf().commands().map().getCooldown());
            }
        } else {
            Faction myFaction = me.getFaction();
            String ownersTo = myFaction.getOwnerListString(to);

            if (changedFaction) {
                me.sendFactionHereMessage(factionFrom, player);
                if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && FactionsPlugin.getInstance().conf().factions().ownedArea().isMessageOnBorder() && myFaction == factionTo && !ownersTo.isEmpty()) {
                    me.sendMessage(TL.GENERIC_OWNERS.format(ownersTo));
                }
            } else if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && FactionsPlugin.getInstance().conf().factions().ownedArea().isMessageInsideTerritory() && myFaction == factionTo && !myFaction.isWilderness()) {
                String ownersFrom = myFaction.getOwnerListString(from);
                if (FactionsPlugin.getInstance().conf().factions().ownedArea().isMessageByChunk() || !ownersFrom.equals(ownersTo)) {
                    if (!ownersTo.isEmpty()) {
                        me.sendMessage(TL.GENERIC_OWNERS.format(ownersTo));
                    } else if (!TL.GENERIC_PUBLICLAND.toString().isEmpty()) {
                        me.sendMessage(TL.GENERIC_PUBLICLAND.toString());
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        EntityType type = event.getRightClicked().getType();

        if (INTERACTABLE_ENTITIES.contains(type)) {
            if (type == EntityType.ITEM_FRAME && !canPlayerUseBlock(event.getPlayer(), Material.ITEM_FRAME, event.getRightClicked().getLocation(), false)) {
                event.setCancelled(true);
                return;
            }
            if (!this.playerCanInteractHere(event.getPlayer(), event.getRightClicked().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerArmorStandManipulateEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        if (!canPlayerUseBlock(event.getPlayer(), Material.ARMOR_STAND, event.getRightClicked().getLocation(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }
        if (!FactionsPlugin.getInstance().isFinishedLoading()) {
            //data not yet loaded
            event.setCancelled(true);
            return;
        }

        // only need to check right-clicks and physical as of MC 1.4+; good performance boost
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null) {
            return;  // clicked in air, apparently
        }

        if (!canPlayerUseBlock(player, block.getType(), block.getLocation(), false)) {
            event.setCancelled(true);
            if (MaterialDb.get().getProvider().isPressurePlate(block.getType())) {
                return;
            }
            if (FactionsPlugin.getInstance().conf().exploits().isInteractionSpam()) {
                int count = interactSpammers.computeIfAbsent(player.getName(), s -> new InteractAttemptSpam()).increment();
                if (count >= 10) {
                    player.sendMessage(TL.PLAYER_OUCH.toString());
                    player.damage(FastMath.floor(count / 10.0D));
                }
            }
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;  // only interested on right-clicks for below
        }

        ItemStack item = event.getItem();
        if (item != null && (MaterialDb.get().getProvider().isMinecart(item.getType()) || item.getType() == Material.ARMOR_STAND || (FactionsPlugin.getInstance().getMCVersion().isAfterOrEq(MinecraftVersions.v1_9) && item.getType() == Material.END_CRYSTAL)) && !FactionsPlugin.getInstance().conf().factions().specialCase().getIgnoreBuildMaterials().contains(item.getType()) &&
                !FactionsBlockListener.playerCanBuildDestroyBlock(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation(), PermissibleAction.BUILD, false)) {
            event.setCancelled(true);
            return;
        }

        if (event.getMaterial() != null && !playerCanUseItemHere(player, block.getLocation(), event.getMaterial(), false)) {
            event.setCancelled(true);
        }
    }


    // for handling people who repeatedly spam attempts to open a door (or similar) in another faction's territory
    private final Map<String, InteractAttemptSpam> interactSpammers = new HashMap<>();

    private static class InteractAttemptSpam {
        private int attempts = 0;
        private long lastAttempt = Instant.now().toEpochMilli();

        // returns the current attempt count
        public int increment() {
            long now = Instant.now().toEpochMilli();
            if (now > lastAttempt + 2000) {
                attempts = 1;
            } else {
                attempts++;
            }
            lastAttempt = now;
            return attempts;
        }
    }

    public boolean playerCanUseItemHere(Player player, Location location, Material material, boolean justCheck) {
        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(player.getName())) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = FLocation.wrap(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (FactionsPlugin.getInstance().getLandRaidControl().isRaidable(otherFaction)) {
            return true;
        }

        if (otherFaction.hasPlayersOnline()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().getTerritoryDenyUsageMaterials().contains(material)) {
                return true; // Item isn't one we're preventing for online factions.
            }
        } else {
            if (!FactionsPlugin.getInstance().conf().factions().protection().getTerritoryDenyUsageMaterialsWhenOffline().contains(material)) {
                return true; // Item isn't one we're preventing for offline factions.
            }
        }

        if (otherFaction.isWilderness()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyUsage() || FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_WILDERNESS, TextUtil.getMaterialName(material));
            }

            return false;
        } else if (otherFaction.isSafeZone()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyUsage() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_SAFEZONE, TextUtil.getMaterialName(material));
            }

            return false;
        } else if (otherFaction.isWarZone()) {
            if (!FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyUsage() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_WARZONE, TextUtil.getMaterialName(material));
            }

            return false;
        }

        if (!otherFaction.hasAccess(me, PermissibleAction.ITEM)) {
            if (!justCheck) {
                me.msg(TL.PLAYER_USE_TERRITORY, TextUtil.getMaterialName(material), otherFaction.getTag(me.getFaction()));
            }
            return false;
        }

        // Also cancel if player doesn't have ownership rights for this claim
        if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyUsage() && !otherFaction.playerHasOwnershipRights(me, loc)) {
            if (!justCheck) {
                me.msg(TL.PLAYER_USE_OWNED, TextUtil.getMaterialName(material), otherFaction.getOwnerListString(loc));
            }

            return false;
        }

        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());

        FactionsPlugin.getInstance().getLandRaidControl().onRespawn(me);

        Location home = me.getFaction().getHome();
        if (FactionsPlugin.getInstance().conf().factions().homes().isEnabled() &&
                FactionsPlugin.getInstance().conf().factions().homes().isTeleportToOnDeath() &&
                home != null &&
                (FactionsPlugin.getInstance().conf().factions().landRaidControl().power().isRespawnHomeFromNoPowerLossWorlds() || !FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getWorldsNoPowerLoss().contains(event.getPlayer().getWorld().getName()))) {
            event.setRespawnLocation(home);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onTeleport(PlayerTeleportEvent event) {
        FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());
        boolean isEnabled = WorldUtil.isEnabled(event.getTo().getWorld());
        if (!isEnabled) {
            me.setShowScoreboard(false);
            if (me.isFlying()) {
                me.setFlying(false);
            }
            return;
        }
        if (!event.getFrom().getWorld().equals(event.getTo().getWorld()) && !WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            FactionsPlugin.getInstance().getLandRaidControl().update(me);
            this.initFactionWorld(me, event.getPlayer());
        }

        FLocation to = FLocation.wrap(event.getTo());
        me.setLastStoodAt(to);

        // Check the location they're teleporting to and check if they can fly there.
        if (FactionsPlugin.getInstance().conf().commands().fly().isEnable() && !me.isAdminBypassing()) {
            boolean canFly = me.canFlyAtLocation(to);
            if (me.isFlying() && !canFly) {
                me.setFlying(false, false);
            } else if (me.isAutoFlying() && !me.isFlying() && canFly) {
                me.setFlying(true);
            }
        }

    }

    // For some reason onPlayerInteract() sometimes misses bucket events depending on distance (something like 2-3 blocks away isn't detected),
    // but these separate bucket events below always fire without fail
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        Block block = event.getBlockClicked();
        Player player = event.getPlayer();

        if (!playerCanUseItemHere(player, block.getLocation(), event.getBucket(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        Block block = event.getBlockClicked();
        Player player = event.getPlayer();

        if (!playerCanUseItemHere(player, block.getLocation(), event.getBucket(), false)) {
            event.setCancelled(true);
        }
    }

    public static boolean preventCommand(String fullCmd, Player player) {
        if ((FactionsPlugin.getInstance().conf().factions().protection().getTerritoryNeutralDenyCommands().isEmpty() &&
                FactionsPlugin.getInstance().conf().factions().protection().getTerritoryEnemyDenyCommands().isEmpty() &&
                FactionsPlugin.getInstance().conf().factions().protection().getPermanentFactionMemberDenyCommands().isEmpty() &&
                FactionsPlugin.getInstance().conf().factions().protection().getWildernessDenyCommands().isEmpty() &&
                FactionsPlugin.getInstance().conf().factions().protection().getTerritoryAllyDenyCommands().isEmpty() &&
                FactionsPlugin.getInstance().conf().factions().protection().getWarzoneDenyCommands().isEmpty())) {
            return false;
        }

        fullCmd = fullCmd.toLowerCase();

        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        String shortCmd;  // command without the slash at the beginning
        if (fullCmd.charAt(0) == '/') {
            shortCmd = fullCmd.substring(1);
        } else {
            shortCmd = fullCmd;
            fullCmd = "/" + fullCmd;
        }

        if (me.hasFaction() &&
                !me.isAdminBypassing() &&
                !FactionsPlugin.getInstance().conf().factions().protection().getPermanentFactionMemberDenyCommands().isEmpty() &&
                me.getFaction().isPermanent() &&
                isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getPermanentFactionMemberDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_PERMANENT, fullCmd);
            return true;
        }

        Faction at = Board.getInstance().getFactionAt(FLocation.wrap(player.getLocation()));
        if (at.isWilderness() && !FactionsPlugin.getInstance().conf().factions().protection().getWildernessDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getWildernessDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_WILDERNESS, fullCmd);
            return true;
        }

        Relation rel = at.getRelationTo(me);
        if (at.isNormal() && rel.isAlly() && !FactionsPlugin.getInstance().conf().factions().protection().getTerritoryAllyDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getTerritoryAllyDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_ALLY, fullCmd);
            return true;
        }

        if (at.isNormal() && rel.isNeutral() && !FactionsPlugin.getInstance().conf().factions().protection().getTerritoryNeutralDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getTerritoryNeutralDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_NEUTRAL, fullCmd);
            return true;
        }

        if (at.isNormal() && rel.isEnemy() && !FactionsPlugin.getInstance().conf().factions().protection().getTerritoryEnemyDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getTerritoryEnemyDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_ENEMY, fullCmd);
            return true;
        }

        if (at.isWarZone() && !FactionsPlugin.getInstance().conf().factions().protection().getWarzoneDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInSet(fullCmd, shortCmd, FactionsPlugin.getInstance().conf().factions().protection().getWarzoneDenyCommands())) {
            me.msg(TL.PLAYER_COMMAND_WARZONE, fullCmd);
            return true;
        }

        return false;
    }

    private static boolean isCommandInSet(String fullCmd, String shortCmd, Set<String> set) {
        for (String string : set) {
            if (string == null) {
                continue;
            }
            string = string.toLowerCase();
            if (fullCmd.startsWith(string) || shortCmd.startsWith(string)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractGUI(InventoryClickEvent event) {
        if (!WorldUtil.isEnabled(event.getWhoClicked().getWorld())) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof GUI) {
            event.setCancelled(true);
            GUI<?> ui = (GUI<?>) event.getClickedInventory().getHolder();
            ui.click(event.getRawSlot(), event.getClick());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMoveGUI(InventoryDragEvent event) {
        if (!WorldUtil.isEnabled(event.getWhoClicked().getWorld())) {
            return;
        }

        if (event.getInventory().getHolder() instanceof GUI) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        FPlayer badGuy = FPlayers.getInstance().getByPlayer(event.getPlayer());
        if (badGuy == null) {
            return;
        }

        // if player was banned (not just kicked), get rid of their stored info
        if (FactionsPlugin.getInstance().conf().factions().other().isRemovePlayerDataWhenBanned() && event.getReason().equals("Banned by admin.")) {
            if (badGuy.getRole() == Role.ADMIN) {
                badGuy.getFaction().promoteNewLeader();
            }

            badGuy.leave(false);
            badGuy.remove();
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!WorldUtil.isEnabled(event.getPlayer().getWorld())) {
            return;
        }

        if (FactionsPlayerListener.preventCommand(event.getMessage(), event.getPlayer())) {
            if (FactionsPlugin.getInstance().logPlayerCommands()) {
                FactionsPlugin.getInstance().getPluginLogger().info("[PLAYER_COMMAND]: &7" + event.getPlayer().getName() + ": " + event.getMessage());
            }
            event.setCancelled(true);
        }
    }
}
