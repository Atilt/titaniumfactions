package com.massivecraft.factions.data.json;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.util.FastMath;

import java.util.Objects;
import java.util.UUID;

public class JSONFPlayer extends MemoryFPlayer {

    public JSONFPlayer(MemoryFPlayer arg0) {
        super(arg0);
    }

    public JSONFPlayer(UUID id) {
        super(Objects.requireNonNull(id, "id"));
    }

    @Override
    public void remove() {
        ((JSONFPlayers) FPlayers.getInstance()).getFPlayers().remove(getId());
    }

    public boolean shouldBeSaved() {
        return this.hasFaction() || (this.getPowerRounded() != this.getPowerMaxRounded() && this.getPowerRounded() != FastMath.round(FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerStarting()));
    }
}
