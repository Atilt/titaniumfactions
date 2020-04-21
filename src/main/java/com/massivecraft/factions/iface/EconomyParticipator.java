package com.massivecraft.factions.iface;

import com.massivecraft.factions.util.TL;

import java.util.UUID;

public interface EconomyParticipator extends RelationParticipator {

    UUID getAccountId();

    void msg(String str, Object... args);

    void msg(TL translation, Object... args);
}
