package com.massivecraft.factions.data;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class MemoryFPlayers extends FPlayers {
    protected final Object2ObjectMap<UUID, FPlayer> fPlayers = new Object2ObjectOpenHashMap<>();
    private final transient ObjectSet<FPlayer> online = new ObjectOpenHashSet<>();

    public Map<UUID, FPlayer> getFPlayers() {
        return fPlayers;
    }

    public void clean() {
        for (FPlayer fplayer : this.fPlayers.values()) {
            if (!Factions.getInstance().isValidFactionId(fplayer.getFactionIdRaw())) {
                FactionsPlugin.getInstance().log("Reset faction data (invalid faction:" + fplayer.getFactionId() + ") for player " + fplayer.getName());
                fplayer.resetFactionData(false);
            }
        }
    }

    public boolean addOnline(FPlayer fPlayer) {
        return this.online.add(fPlayer);
    }

    public boolean removeOnline(FPlayer fPlayer) {
        return this.online.remove(fPlayer);
    }

    public Collection<FPlayer> getOnlinePlayers() {
        return new ObjectOpenHashSet<>(this.online);
    }

    @Override
    public FPlayer getByPlayer(Player player) {
        return getById(player.getUniqueId());
    }

    @Override
    public List<FPlayer> getAllFPlayers() {
        return new ObjectArrayList<>(fPlayers.values());
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
}
