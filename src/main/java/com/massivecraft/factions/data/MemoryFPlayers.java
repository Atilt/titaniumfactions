package com.massivecraft.factions.data;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class MemoryFPlayers extends FPlayers {
    protected final Map<UUID, FPlayer> fPlayers = new HashMap<>();
    private final transient Set<FPlayer> online = new HashSet<>();

    public Map<UUID, FPlayer> getFPlayers() {
        return fPlayers;
    }

    public void clean() {
        for (FPlayer fplayer : this.fPlayers.values()) {
            if (!Factions.getInstance().isValidFactionId(fplayer.getFactionIdRaw())) {
                FactionsPlugin.getInstance().getPluginLogger().info("Reset faction data (invalid faction:" + fplayer.getFactionId() + ") for player " + fplayer.getName());
                fplayer.resetFactionData(false);
            }
        }
    }

    @Override
    public boolean addOnline(FPlayer fPlayer) {
        return this.online.add(fPlayer);
    }

    @Override
    public boolean removeOnline(FPlayer fPlayer) {
        return this.online.remove(fPlayer);
    }

    @Override
    public boolean isOnline(FPlayer fPlayer) {
        return this.online.contains(fPlayer);
    }

    @Override
    public void wipeOnline() {
        this.online.clear();
    }

    @Override
    public int onlineSize() {
        return this.online.size();
    }

    public Collection<FPlayer> getOnlinePlayers() {
        return new HashSet<>(this.online);
    }

    @Override
    public FPlayer getByPlayer(Player player) {
        return getById(player.getUniqueId());
    }

    @Override
    public List<FPlayer> getAllFPlayers() {
        return new ArrayList<>(fPlayers.values());
    }

    @Override
    public FPlayer getByOfflinePlayer(OfflinePlayer player) {
        return getById(player.getUniqueId());
    }

    @Override
    public FPlayer getById(UUID id) {
        return fPlayers.computeIfAbsent(id, this::generateFPlayer);
    }

    protected abstract FPlayer generateFPlayer(UUID id);

    public abstract void convertFrom(MemoryFPlayers old, BooleanConsumer finish);

    @Override
    public Iterator<FPlayer> iterator() {
        return this.online.iterator();
    }
}
