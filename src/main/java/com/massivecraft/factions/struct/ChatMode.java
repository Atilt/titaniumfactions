package com.massivecraft.factions.struct;

import com.massivecraft.factions.util.TL;

public enum ChatMode {
    COLEADER(5, TL.CHAT_COLEADER),
    MOD(4, TL.CHAT_MOD),
    FACTION(3, TL.CHAT_FACTION),
    ALLIANCE(2, TL.CHAT_ALLIANCE),
    TRUCE(1, TL.CHAT_TRUCE),
    PUBLIC(0, TL.CHAT_PUBLIC);

    public final int value;
    public final TL nicename;

    private static final ChatMode[] VALUES = ChatMode.values();

    ChatMode(int value, final TL nicename) {
        this.value = value;
        this.nicename = nicename;
    }

    public boolean isAtLeast(ChatMode role) {
        return this.value >= role.value;
    }

    public boolean isAtMost(ChatMode role) {
        return this.value <= role.value;
    }

    @Override
    public String toString() {
        return this.nicename.toString();
    }

    public ChatMode getNext() {
        return VALUES[(this.ordinal() + 1) % VALUES.length];
    }
}
