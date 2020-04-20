package com.massivecraft.factions.data;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.util.FastUUID;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class MemoryFPlayers extends FPlayers {
    protected final Map<String, FPlayer> fPlayers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, FPlayer> getFPlayers() {
        return fPlayers;
    }

    public void clean() {
        for (FPlayer fplayer : this.fPlayers.values()) {
            if (!Factions.getInstance().isValidFactionId(fplayer.getFactionId())) {
                FactionsPlugin.getInstance().log("Reset faction data (invalid faction:" + fplayer.getFactionId() + ") for player " + fplayer.getName());
                fplayer.resetFactionData(false);
            }
        }
    }

    public Collection<FPlayer> getOnlinePlayers() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        ObjectSet<FPlayer> entities = new ObjectOpenHashSet<>(players.size());
        for (Player player : players) {
            entities.add(this.getByPlayer(player));
        }
        return entities;
    }

    @Override
    public FPlayer getByPlayer(Player player) {
        return getById(FastUUID.toString(player.getUniqueId()));
    }

    @Override
    public List<FPlayer> getAllFPlayers() {
        return new ObjectArrayList<>(fPlayers.values());
    }

    @Override
    public FPlayer getByOfflinePlayer(OfflinePlayer player) {
        return getById(FastUUID.toString(player.getUniqueId()));
    }

    @Override
    public FPlayer getById(String id) {
        FPlayer player = fPlayers.get(id);
        return player == null ? generateFPlayer(id) : player;
    }

    protected abstract FPlayer generateFPlayer(String id);

    public abstract void convertFrom(MemoryFPlayers old, BooleanConsumer finish);
}
