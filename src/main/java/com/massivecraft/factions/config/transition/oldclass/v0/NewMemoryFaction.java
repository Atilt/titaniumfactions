package com.massivecraft.factions.config.transition.oldclass.v0;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.util.LazyLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NewMemoryFaction {
    private final String id;
    private final boolean peacefulExplosionsEnabled;
    private final boolean permanent;
    private final String tag;
    private final String description;
    private final boolean open;
    private final boolean peaceful;
    private final Integer permanentPower;
    private final LazyLocation home;
    private final long foundedDate;
    private final double powerBoost;
    private final Map<String, Relation> relationWish;
    private final Map<FLocation, Set<String>> claimOwnership;
    private final Set<String> invites;
    private final HashMap<String, List<String>> announcements;
    private final ConcurrentHashMap<String, LazyLocation> warps;
    private final ConcurrentHashMap<String, String> warpPasswords;
    private final long lastDeath;
    private final int maxVaults;
    private final Role defaultRole;
    private final Map<Permissible, Map<PermissibleAction, Boolean>> permissions;
    private final Map<Permissible, Map<PermissibleAction, Boolean>> permissionsOffline;
    private final Set<BanInfo> bans;

    public NewMemoryFaction(OldMemoryFactionV0 old) {
        this.id = old.id;
        this.peacefulExplosionsEnabled = old.peacefulExplosionsEnabled;
        this.permanent = old.permanent;
        this.tag = old.tag;
        this.description = old.description;
        this.open = old.open;
        this.peaceful = old.peaceful;
        this.permanentPower = old.permanentPower;
        this.home = old.home;
        this.foundedDate = old.foundedDate;
        this.powerBoost = old.powerBoost;
        this.relationWish = old.relationWish;
        this.claimOwnership = old.claimOwnership;
        this.invites = old.invites;
        this.announcements = old.announcements;
        this.warps = old.warps;
        this.warpPasswords = old.warpPasswords;
        this.lastDeath = old.lastDeath;
        this.maxVaults = old.maxVaults;
        this.defaultRole = old.defaultRole;
        this.permissions = new HashMap<>();
        this.permissionsOffline = new HashMap<>();
        old.permissions.forEach((permiss, map) -> {
            Map<PermissibleAction, Boolean> newMap = new HashMap<>();
            map.forEach((permact, access) -> {
                if (permact == null) {
                    return;
                }
                if (access == OldAccessV0.ALLOW || access == OldAccessV0.DENY) {
                    newMap.put(permact.getNew(), access == OldAccessV0.ALLOW);
                }
            });
            this.permissions.put(permiss.newPermissible(), newMap);
            this.permissionsOffline.put(permiss.newPermissible(), newMap);
        });
        this.bans = old.bans;
    }
}