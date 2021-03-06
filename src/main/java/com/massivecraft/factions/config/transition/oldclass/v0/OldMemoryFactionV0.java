package com.massivecraft.factions.config.transition.oldclass.v0;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.util.LazyLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OldMemoryFactionV0 {
    protected final String id = null;
    protected boolean peacefulExplosionsEnabled;
    protected boolean permanent;
    protected String tag;
    protected String description;
    protected boolean open;
    protected boolean peaceful;
    protected Integer permanentPower;
    protected LazyLocation home;
    protected long foundedDate;
    protected double money;
    protected double powerBoost;
    protected final Map<String, Relation> relationWish = new HashMap<>();
    protected final Map<FLocation, Set<String>> claimOwnership = new ConcurrentHashMap<>();
    protected final Set<String> invites = new HashSet<>();
    protected final HashMap<String, List<String>> announcements = new HashMap<>();
    protected final ConcurrentHashMap<String, LazyLocation> warps = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, String> warpPasswords = new ConcurrentHashMap<>();
    protected long lastDeath;
    protected int maxVaults;
    protected Role defaultRole;
    protected final Map<OldPermissableV0, Map<OldPermissableActionV0, OldAccessV0>> permissions = new HashMap<>();
    protected final Set<BanInfo> bans = new HashSet<>();

    private OldMemoryFactionV0() {
    }
}