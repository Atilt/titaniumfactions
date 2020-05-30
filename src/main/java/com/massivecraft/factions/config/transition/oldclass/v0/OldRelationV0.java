package com.massivecraft.factions.config.transition.oldclass.v0;

import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.TL;


public enum OldRelationV0 implements OldPermissableV0 {
    MEMBER(4, TL.RELATION_MEMBER_SINGULAR.toString(), Relation.MEMBER),
    ALLY(3, TL.RELATION_ALLY_SINGULAR.toString(), Relation.ALLY),
    TRUCE(2, TL.RELATION_TRUCE_SINGULAR.toString(), Relation.TRUCE),
    NEUTRAL(1, TL.RELATION_NEUTRAL_SINGULAR.toString(), Relation.NEUTRAL),
    ENEMY(0, TL.RELATION_ENEMY_SINGULAR.toString(), Relation.ENEMY);

    public final int value;
    public final String nicename;
    public final Relation replacement;

    OldRelationV0(final int value, final String nicename, final Relation replacement) {
        this.value = value;
        this.nicename = nicename;
        this.replacement = replacement;
    }

    public Permissible newPermissible() {
        return this.replacement;
    }

    public static OldRelationV0 fromString(String s) {
        if (s.equalsIgnoreCase(MEMBER.toString())) {
            return MEMBER;
        } else if (s.equalsIgnoreCase(ALLY.toString())) {
            return ALLY;
        } else if (s.equalsIgnoreCase(TRUCE.toString())) {
            return TRUCE;
        } else if (s.equalsIgnoreCase(ENEMY.toString())) {
            return ENEMY;
        } else {
            return NEUTRAL;
        }
    }

    @Override
    public String toString() {
        return this.nicename;
    }
}
