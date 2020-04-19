package com.massivecraft.factions.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

public class FTeamWrapper {
    private static final Object2ObjectMap<Faction, FTeamWrapper> wrappers = new Object2ObjectOpenHashMap<>();
    private static final ObjectList<FScoreboard> tracking = new ObjectArrayList<>();
    private static int factionTeamPtr;
    private static final ObjectSet<Faction> updating = new ObjectOpenHashSet<>();

    private final Object2ObjectMap<FScoreboard, Team> teams = new Object2ObjectOpenHashMap<>();
    private final String teamName;
    private final Faction faction;
    private final ObjectSet<OfflinePlayer> members = new ObjectOpenHashSet<>();

    public static void applyUpdatesLater(final Faction faction) {
        if (!FScoreboard.isSupportedByServer()) {
            return;
        }

        if (faction.isWilderness()) {
            return;
        }

        if (!FactionsPlugin.getInstance().conf().scoreboard().constant().isPrefixes()) {
            return;
        }


        if (updating.add(faction)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    updating.remove(faction);
                    applyUpdates(faction);
                }
            }.runTask(FactionsPlugin.getInstance());
        }
    }

    public static void applyUpdates(Faction faction) {
        if (!FScoreboard.isSupportedByServer()) {
            return;
        }

        if (faction.isWilderness()) {
            return;
        }

        if (!FactionsPlugin.getInstance().conf().scoreboard().constant().isPrefixes()) {
            return;
        }

        if (updating.contains(faction)) {
            // Faction will be updated soon.
            return;
        }

        FTeamWrapper wrapper = wrappers.get(faction);
        Set<FPlayer> factionMembers = faction.getFPlayers();

        if (wrapper != null && Factions.getInstance().getFactionById(faction.getId()) == null) {
            // Faction was disbanded
            wrapper.unregister();
            wrappers.remove(faction);
            return;
        }

        if (wrapper == null) {
            wrapper = new FTeamWrapper(faction);
            wrappers.put(faction, wrapper);
        }

        for (OfflinePlayer player : wrapper.getPlayers()) {
            if (!player.isOnline() || !factionMembers.contains(FPlayers.getInstance().getByOfflinePlayer(player))) {
                // Player is offline or no longer in faction
                wrapper.removePlayer(player);
            }
        }

        for (FPlayer fmember : factionMembers) {
            if (!fmember.isOnline()) {
                continue;
            }

            // Scoreboard might not have player; add him/her
            wrapper.addPlayer(fmember.getPlayer());
        }

        wrapper.updatePrefixes();
    }

    public static void updatePrefixes(Faction faction) {
        if (!FScoreboard.isSupportedByServer()) {
            return;
        }

        if (!wrappers.containsKey(faction)) {
            applyUpdates(faction);
        } else {
            wrappers.get(faction).updatePrefixes();
        }
    }

    protected static void track(FScoreboard fboard) {
        if (!FScoreboard.isSupportedByServer()) {
            return;
        }
        tracking.add(fboard);
        for (FTeamWrapper wrapper : wrappers.values()) {
            wrapper.add(fboard);
        }
    }

    protected static void untrack(FScoreboard fboard) {
        if (!FScoreboard.isSupportedByServer()) {
            return;
        }
        tracking.remove(fboard);
        for (FTeamWrapper wrapper : wrappers.values()) {
            wrapper.remove(fboard);
        }
    }


    private FTeamWrapper(Faction faction) {
        this.teamName = "faction_" + (factionTeamPtr++);
        this.faction = faction;

        for (FScoreboard fboard : tracking) {
            add(fboard);
        }
    }

    private void add(FScoreboard fboard) {
        Scoreboard board = fboard.getScoreboard();
        Team team = board.registerNewTeam(teamName);
        teams.put(fboard, team);

        for (OfflinePlayer player : getPlayers()) {
            team.addPlayer(player);
        }

        updatePrefix(fboard);
    }

    private void remove(FScoreboard fboard) {
        teams.remove(fboard).unregister();
    }

    private void updatePrefixes() {
        if (FactionsPlugin.getInstance().conf().scoreboard().constant().isPrefixes()) {
            for (FScoreboard fboard : teams.keySet()) {
                updatePrefix(fboard);
            }
        }
    }

    private void updatePrefix(FScoreboard fboard) {
        if (FactionsPlugin.getInstance().conf().scoreboard().constant().isPrefixes()) {
            FPlayer fplayer = fboard.getFPlayer();
            Team team = teams.get(fboard);

            String prefix = TL.DEFAULT_PREFIX.toString();
            prefix = Tag.parsePlaceholders(fplayer.getPlayer(), prefix);
            prefix = prefix.replace("{relationcolor}", faction.getRelationTo(fplayer).getColor().toString());
            prefix = prefix.replace("{faction}", faction.getTag().substring(0, Math.min("{faction}".length() + FactionsPlugin.getInstance().conf().scoreboard().constant().getPrefixLength() - prefix.length(), faction.getTag().length())));
            if (!prefix.equals(team.getPrefix())) {
                team.setPrefix(prefix);
            }
        }
    }

    private void addPlayer(OfflinePlayer player) {
        if (members.add(player)) {
            for (Team team : teams.values()) {
                team.addPlayer(player);
            }
        }
    }

    private void removePlayer(OfflinePlayer player) {
        if (members.remove(player)) {
            for (Team team : teams.values()) {
                team.removePlayer(player);
            }
        }
    }

    private Set<OfflinePlayer> getPlayers() {
        return new ObjectOpenHashSet<>(this.members);
    }

    private void unregister() {
        for (Team team : teams.values()) {
            team.unregister();
        }
        teams.clear();
    }
}
