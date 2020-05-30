package com.massivecraft.factions.iface;

import com.massivecraft.factions.util.TL;

import java.util.UUID;

public interface EconomyParticipator extends RelationParticipator {

    UUID getAccountId();

    void msg(String msg);

     void msg(TL translation);

     void msg(TL translation, TL toAppend);

     void msg(TL translation, String... args);
}
