package com.massivecraft.factions;

import com.massivecraft.factions.data.json.JSONFPlayers;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.function.IntConsumer;

public abstract class FPlayers {
    protected static FPlayers instance = getFPlayersImpl();

    public abstract void clean();

    public static FPlayers getInstance() {
        return instance;
    }

    private static FPlayers getFPlayersImpl() {
        // TODO switch on configuration backend
        return new JSONFPlayers();
    }

    public abstract boolean addOnline(FPlayer fPlayer);

    public abstract boolean removeOnline(FPlayer fPlayer);

    public abstract Collection<FPlayer> getOnlinePlayers();

    public abstract FPlayer getByPlayer(Player player);

    public abstract Collection<FPlayer> getAllFPlayers();

    public abstract void forceSave(BooleanConsumer finish);

    public abstract void forceSave(boolean sync, BooleanConsumer finish);

    public abstract FPlayer getByOfflinePlayer(OfflinePlayer player);

    public abstract FPlayer getById(UUID id);

    public abstract void load(IntConsumer loaded);
}
