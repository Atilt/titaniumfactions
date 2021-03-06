package com.massivecraft.factions.config.file;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.WipeOnReload;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.material.FactionMaterial;
import me.lucko.helper.reflect.MinecraftVersions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"FieldCanBeLocal", "InnerClassMayBeStatic", "BooleanMethodIsAlwaysInverted"})
public class MainConfig {
    public static class AVeryFriendlyFactionsConfig {
        @Comment("Don't change this value yourself, unless you WANT a broken config!")
        private final int version = 3;

        @Comment("Debug\n" +
                "Turn this on if you are having issues with something and working on resolving them.\n" +
                "This will spam your console with information that is useful if you know how to read the source.\n" +
                "It's suggested that you only turn this on at the direction of a developer.")
        private final boolean debug = false;

        public boolean isDebug() {
            return debug;
        }
    }

    public static class Lang {
        @Comment("Determines if the language file has been transitioned to the new format. By default, the old Factions\n" +
                "language file will be converted to a format such as '{0}, {1}, {2}, etc.' for arguments. If this is set to false,\n" +
                "the language file will transitioned on startup.")
        private boolean langTransitioned = false;

        @Comment("This is the format type that the language file will use. There are currently two types: INDEX and LOOSE.\n" +
                "INDEX is the default format. This format allows you to specify what arguments go where in the message, hence why you\n" +
                "can specify indexes in the format ({0}, {1}, etc.). LOOSE does not give you the option to specify argument indexes in the format ([], []).\n" +
                "LOOSE will give better performance over INDEX, given you don't mind losing the ability to customize argument placement in messages."
        )
        private final String formatType = "INDEX";

        public boolean isLangTransitioned() {
            return langTransitioned;
        }

        public void setLangTransitioned(boolean langTransitioned) {
            this.langTransitioned = langTransitioned;
        }

        public String getFormatType() {
            return formatType;
        }
    }

    public class Tablist {
        private final boolean enabled = false;

        private final String header = "&f&lA Factions Server Header";
        private final String footer = "&7&lA Factions Server Footer";

        public boolean isEnabled() {
            return this.enabled;
        }

        public String getHeader() {
            return this.header;
        }

        public String getFooter() {
            return this.footer;
        }

        public String getHeaderColored() {
            return TextUtil.parseColorBukkit(this.header);
        }

        public String getFooterColored() {
            return TextUtil.parseColorBukkit(this.footer);
        }
    }

    public class Colors {
        public class Relations {
            private final String member = "GREEN";
            @WipeOnReload
            private transient ChatColor memberColor;
            private final String ally = "LIGHT_PURPLE";
            @WipeOnReload
            private transient ChatColor allyColor;
            private final String truce = "DARK_PURPLE";
            @WipeOnReload
            private transient ChatColor truceColor;
            private final String neutral = "WHITE";
            @WipeOnReload
            private transient ChatColor neutralColor;
            private final String enemy = "RED";
            @WipeOnReload
            private transient ChatColor enemyColor;
            private final String peaceful = "GOLD";
            @WipeOnReload
            private transient ChatColor peacefulColor;

            public ChatColor getMember() {
                return memberColor = Colors.this.getColor(this.member, this.memberColor, ChatColor.GREEN);
            }

            public ChatColor getAlly() {
                return allyColor = Colors.this.getColor(this.ally, this.allyColor, ChatColor.LIGHT_PURPLE);
            }

            public ChatColor getTruce() {
                return truceColor = Colors.this.getColor(this.truce, this.truceColor, ChatColor.DARK_PURPLE);
            }

            public ChatColor getNeutral() {
                return neutralColor = Colors.this.getColor(this.neutral, this.neutralColor, ChatColor.WHITE);
            }

            public ChatColor getEnemy() {
                return enemyColor = Colors.this.getColor(this.enemy, this.enemyColor, ChatColor.RED);
            }

            public ChatColor getPeaceful() {
                return peacefulColor = Colors.this.getColor(this.peaceful, this.peacefulColor, ChatColor.GOLD);
            }
        }

        public class Factions {
            private final String wilderness = "GRAY";
            @WipeOnReload
            private transient ChatColor wildernessColor;
            private final String safezone = "GOLD";
            @WipeOnReload
            private transient ChatColor safezoneColor;
            private final String warzone = "DARK_RED";
            @WipeOnReload
            private transient ChatColor warzoneColor;

            public ChatColor getWilderness() {
                return wildernessColor = Colors.this.getColor(this.wilderness, this.wildernessColor, ChatColor.GRAY);
            }

            public ChatColor getSafezone() {
                return safezoneColor = Colors.this.getColor(this.safezone, this.safezoneColor, ChatColor.GOLD);
            }

            public ChatColor getWarzone() {
                return warzoneColor = Colors.this.getColor(this.warzone, this.warzoneColor, ChatColor.DARK_RED);
            }
        }

        private final Factions factions = new Factions();
        private final Relations relations = new Relations();

        private ChatColor getColor(String name, ChatColor current, ChatColor defaultColor) {
            if (current != null) {
                return current;
            }
            try {
                return ChatColor.valueOf(name);
            } catch (IllegalArgumentException e) {
                return defaultColor;
            }
        }

        public Factions factions() {
            return factions;
        }

        public Relations relations() {
            return relations;
        }
    }

    public class Commands {
        public class Fly {
            public class Particles {
                @Comment("Speed of the particles, can be decimal value")
                private final double speed = 0.02;
                @Comment("Amount spawned")
                private final int amount = 20;
                @Comment("How often should we spawn these particles?\n" +
                        "0 disables this completely")
                private final double spawnRate = 0.2;

                public double getSpeed() {
                    return speed;
                }

                public int getAmount() {
                    return amount;
                }

                public double getSpawnRate() {
                    return spawnRate;
                }
            }

            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private final int delay = 0;
            @Comment("True to enable the fly command, false to disable")
            private final boolean enable = true;
            @Comment("If a player leaves fly (out of territory or took damage)\n" +
                    "how long (in seconds) should they not take fall damage for?\n" +
                    "Set to 0 to have them always take fall damage.")
            private final int fallDamageCooldown = 3;
            @Comment("From how far away a player can disable another's flight by being enemy\n" +
                    "Set to 0 if wanted disable\n" +
                    "Note: Will produce lag at higher numbers")
            private final int enemyRadius = 7;
            @Comment("How frequently to check enemy radius, in seconds. Set to 0 to disable checking.")
            private final int radiusCheck = 1;
            @Comment("Should we disable flight if the player has suffered generic damage")
            private final boolean disableOnGenericDamage = false;

            @Comment("Trails show below the players foot when flying, faction.fly.trails\n" +
                    "Players can enable them with /f trail on/off\n" +
                    "Players can also set which effect to show /f trail effect <particle> only if they have faction.fly.trails.<particle>")
            private final Particles particles = new Particles();

            public int getDelay() {
                return delay;
            }

            public boolean isEnable() {
                return enable;
            }

            public int getFallDamageCooldown() {
                return fallDamageCooldown;
            }

            public int getEnemyRadius() {
                return enemyRadius;
            }

            public int getRadiusCheck() {
                return radiusCheck;
            }

            public boolean isDisableOnGenericDamage() {
                return disableOnGenericDamage;
            }

            public Particles particles() {
                return particles;
            }
        }

        public class Help {
            @Comment("You can change the page name to whatever you like\n" +
                    "We use '1' to preserve default functionality of /f help 1")
            private final Map<String, List<String>> entries = new HashMap<String, List<String>>() {
                {
                    this.put("1", Arrays.asList(
                            "&e&m----------------------------------------------",
                            "                  &c&lFactions Help               ",
                            "&e&m----------------------------------------------",
                            "&3/f create  &e>>  &7Create your own faction",
                            "&3/f who      &e>>  &7Show factions info",
                            "&3/f tag      &e>>  &7Change faction tag",
                            "&3/f join     &e>>  &7Join faction",
                            "&3/f list      &e>>  &7List all factions",
                            "&e&m--------------&r &2/f help 2 for more &e&m--------------"));
                    this.put("2", Arrays.asList(
                            "&e&m------------------&r&c&l Page 2 &e&m--------------------",
                            "&3/f home     &e>>  &7Teleport to faction home",
                            "&3/f sethome &e>>  &7Set your faction home",
                            "&3/f leave    &e>>  &7Leave your faction",
                            "&3/f invite    &e>>  &7Invite a player to your faction",
                            "&3/f deinvite &e>>  &7Revoke invitation to player",
                            "&e&m--------------&r &2/f help 3 for more &e&m--------------"));
                    this.put("3", Arrays.asList(
                            "&e&m------------------&r&c&l Page 3 &e&m--------------------",
                            "&3/f claim     &e>>  &7Claim land",
                            "&3/f unclaim  &e>>  &7Unclaim land",
                            "&3/f kick      &e>>  &7Kick player from your faction",
                            "&3/f mod      &e>>  &7Set player role in faction",
                            "&3/f chat     &e>>  &7Switch to faction chat",
                            "&e&m--------------&r &2/f help 4 for more &e&m--------------"));
                    this.put("4", Arrays.asList(
                            "&e&m------------------&r&c&l Page 4 &e&m--------------------",
                            "&3/f version &e>>  &7Display version information",
                            "&e&m--------------&r&2 End of /f help &e&m-----------------"));
                }
            };
            @Comment("set to true to use legacy factions help")
            private final boolean useOldHelp = true;

            public Map<String, List<String>> getEntries() {
                return entries != null ? Collections.unmodifiableMap(entries) : Collections.emptyMap();
            }

            public boolean isUseOldHelp() {
                return useOldHelp;
            }
        }

        public class Home {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private final int delay = 0;

            public int getDelay() {
                return delay;
            }
        }

        public class ListCmd {
            @Comment("You can only use {pagenumber} and {pagecount} in the header.\nBlank entry results in nothing being displayed.")
            private final String header = "&e&m----------&r&e[ &2Faction List &9{pagenumber}&e/&9{pagecount} &e]&m----------";
            @Comment("You can only use {pagenumber} and {pagecount} in the footer.\nBlank entry results in nothing being displayed.")
            private final String footer = "";
            @Comment("You can use any variables here")
            private final String factionlessEntry = "<i>Factionless<i> {factionless} online";
            @Comment("You can use any variable here")
            private final String entry = "<a>{faction-relation-color}{faction} <i>{online} / {members} online, <a>Land / Power / Maxpower: <i>{chunks}/{power}/{maxPower}";

            public String getHeader() {
                return header;
            }

            public String getFooter() {
                return footer;
            }

            public String getFactionlessEntry() {
                return factionlessEntry;
            }

            public String getEntry() {
                return entry;
            }
        }

        public class MapCmd {
            @Comment("This will help limit how many times a player can be sent a map of factions.\n" +
                    "Set this to the cooldown you want, in milliseconds, for a map to be shown to a player.\n" +
                    "This can prevent some teleportation-based exploits for finding factions.\n" +
                    "The old default was 2000, which blocks any movement faster than running.\n" +
                    "The new default is 700, which should also allow boats and horses.")
            private final int cooldown = 700;

            public int getCooldown() {
                return cooldown;
            }
        }

        public class Near {
            @Comment("Making this radius larger increases lag, do so at your own risk\n" +
                    "If on a high radius it is advised to add a cooldown to the command\n" +
                    "Also using {distance} placeholder in the lang would cause more lag on a bigger radius")
            private final int radius = 20;

            public int getRadius() {
                return radius;
            }
        }

        public class SeeChunk {
            private final boolean particles = true;
            @Comment("Get a list of particle names here: https://factions.support/particles/")
            private final String particleName = "REDSTONE";
            @Comment("If the chosen particle is compatible with coloring we will color\n" +
                    "it based on the current chunk's faction")
            private final boolean relationalColor = true;
            @Comment("How often should we update the particles to the current player's location?")
            private final double particleUpdateTime = 0.75;

            public boolean isParticles() {
                return particles;
            }

            public String getParticleName() {
                return particleName;
            }

            public boolean isRelationalColor() {
                return relationalColor;
            }

            public double getParticleUpdateTime() {
                return particleUpdateTime;
            }
        }

        public class Show {
            @Comment("You can use any variable here, including fancy messages. Color codes and or tags work fine.\n" +
                    "Lines that aren't defined wont be sent (home not set, faction not peaceful / permanent, dtr freeze)\n" +
                    "Supports placeholders.\n" +
                    "First line can be {header} for default header, or any string (we recommend &m for smooth lines ;p)\n" +
                    "The line with 'permanent' in it only appears if the faction is permanent.")
            private final List<String> format = new ArrayList<String>() {
                {
                    this.add("{header}");
                    this.add("<a>Description: <i>{description}");
                    this.add("<a>Joining: <i>{joining}    {peaceful}");
                    this.add("<a>Land / Power / Maxpower: <i> {chunks}/{power}/{maxPower}");
                    this.add("<a>Raidable: {raidable}");
                    this.add("<a>Founded: <i>{create-date}");
                    this.add("<a>This faction is permanent, remaining even with no members.'");
                    this.add("<a>Land value: <i>{land-value} {land-refund}");
                    this.add("<a>Balance: <i>{faction-balance}");
                    this.add("<a>Bans: <i>{faction-bancount}");
                    this.add("<a>Allies(<i>{allies}<a>/<i>{max-allies}<a>): {allies-list} ");
                    this.add("<a>Online: (<i>{online}<a>/<i>{members}<a>): {online-list}");
                    this.add("<a>Offline: (<i>{offline}<a>/<i>{members}<a>): {offline-list}");
                }
            };
            @Comment("Set true to not display empty fancy messages")
            private final boolean minimal = false;
            @Comment("Factions that should be exempt from /f show, case sensitive, useful for a\n" +
                    "serverteam faction, since the command shows vanished players otherwise")
            private final List<String> exempt = new ArrayList<String>() {
                {
                    this.add("put_faction_tag_here");
                }
            };

            public List<String> getFormat() {
                return format != null ? Collections.unmodifiableList(format) : Collections.emptyList();
            }

            public boolean isMinimal() {
                return minimal;
            }

            public List<String> getExempt() {
                return exempt != null ? Collections.unmodifiableList(exempt) : Collections.emptyList();
            }
        }

        public class Stuck {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private final int delay = 30;
            @Comment("This radius defines how far from where they ran the command the player\n" +
                    "may travel while waiting to be unstuck. If they leave this radius, the\n" +
                    "command will be cancelled.")
            private final int radius = 10;

            public int getDelay() {
                return delay;
            }

            public int getRadius() {
                return radius;
            }
        }

        public class TNT {
            private final boolean enable = false;
            @Comment("Maximum storage. Set to -1 (or lower) to disable")
            private final int maxStorage = -1;
            private final int maxRadius = 5;

            public int getMaxRadius() {
                return maxRadius;
            }

            public int getMaxStorage() {
                return maxStorage;
            }

            public boolean isAboveMaxStorage(int amount) {
                if (maxStorage < 0) {
                    return false;
                }
                return amount > maxStorage;
            }

            public boolean isEnable() {
                return enable;
            }
        }

        public class Warp {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private final int delay = 0;
            @Comment("What should be the maximum amount of warps that a Faction can set?")
            private final int maxWarps = 5;

            public int getDelay() {
                return delay;
            }

            public int getMaxWarps() {
                return maxWarps;
            }
        }

        public class ToolTips {
            @Comment("Faction on-hover tooltip information")
            private final List<String> faction = new ArrayList<String>() {
                {
                    this.add("&6Leader: &f{leader}");
                    this.add("&6Claimed: &f{chunks}");
                    this.add("&6Raidable: &f{raidable}");
                    this.add("&6Warps: &f{warps}");
                    this.add("&6Power: &f{power}/{maxPower}");
                    this.add("&6Members: &f{online}/{members}");
                }
            };
            @Comment("Player on-hover tooltip information")
            private final List<String> player = new ArrayList<String>() {
                {
                    this.add("&6Last Seen: &f{lastSeen}");
                    this.add("&6Power: &f{player-power}");
                    this.add("&6Rank: &f{group}");
                    this.add("&6Balance: &a${balance}");
                }
            };

            public List<String> getFaction() {
                return faction != null ? Collections.unmodifiableList(faction) : Collections.emptyList();
            }

            public List<String> getPlayer() {
                return player != null ? Collections.unmodifiableList(player) : Collections.emptyList();
            }
        }

        private final Fly fly = new Fly();
        private final Help help = new Help();
        private final Home home = new Home();
        private final ListCmd list = new ListCmd();
        private final MapCmd map = new MapCmd();
        private final Near near = new Near();
        private final SeeChunk seeChunk = new SeeChunk();
        private final Show show = new Show();
        private final Stuck stuck = new Stuck();
        @Comment("TNT bank!")
        private final TNT tnt = new TNT();
        private final ToolTips toolTips = new ToolTips();
        private final Warp warp = new Warp();

        public Fly fly() {
            return fly;
        }

        public Help help() {
            return help;
        }

        public Home home() {
            return home;
        }

        public ListCmd list() {
            return list;
        }

        public MapCmd map() {
            return map;
        }

        public Near near() {
            return near;
        }

        public SeeChunk seeChunk() {
            return seeChunk;
        }

        public Show show() {
            return show;
        }

        public Stuck stuck() {
            return stuck;
        }

        public TNT tnt() {
            return tnt;
        }

        public ToolTips toolTips() {
            return toolTips;
        }

        public Warp warp() {
            return warp;
        }
    }

    public class Factions {
        public class LandRaidControl {
            public class DTR {
                private final double startingDTR = 2.0;
                private final double maxDTR = 10.0;
                private final double minDTR = -3.0;
                private final double perPlayer = 1;
                private final double regainPerMinutePerPlayer = 0.05;
                private final double regainPerMinuteMaxRate = 0.1;
                private final double lossPerDeath = 1;
                @Comment("Time, in seconds, to freeze DTR regeneration after a faction member dies")
                private final int freezeTime = 0;
                private final boolean freezePreventsJoin = true;
                private final boolean freezePreventsLeave = true;
                private final boolean freezePreventsDisband = true;
                private final double freezeKickPenalty = 0.5;
                private final String freezeTimeFormat = "H:mm:ss";
                @Comment("Additional claims allowed for each player in the faction")
                private final int landPerPlayer = 3;
                @Comment("Claims the faction starts with.\n" +
                        "Note: A faction of one player has this many PLUS the perPlayer amount.")
                private final int landStarting = 6;
                private final int decimalDigits = 2;
                private Map<String, Number> worldDeathModifiers = new HashMap<String, Number>() {
                    {
                        this.put("world_nether", 0.5D);
                        this.put("world_the_end", 0.25D);
                    }
                };

                public int getDecimalDigits() {
                    return decimalDigits;
                }

                public int getLandPerPlayer() {
                    return landPerPlayer;
                }

                public int getLandStarting() {
                    return landStarting;
                }

                public int getFreezeTime() {
                    return freezeTime;
                }

                public String getFreezeTimeFormat() {
                    return freezeTimeFormat;
                }

                public boolean isFreezePreventsJoin() {
                    return freezePreventsJoin;
                }

                public boolean isFreezePreventsLeave() {
                    return freezePreventsLeave;
                }

                public boolean isFreezePreventsDisband() {
                    return freezePreventsDisband;
                }

                public double getFreezeKickPenalty() {
                    return freezeKickPenalty;
                }

                public double getMinDTR() {
                    return minDTR;
                }

                public double getPerPlayer() {
                    return perPlayer;
                }

                public double getRegainPerMinutePerPlayer() {
                    return regainPerMinutePerPlayer;
                }

                public double getRegainPerMinuteMaxRate() {
                    return regainPerMinuteMaxRate;
                }

                public double getMaxDTR() {
                    return maxDTR;
                }

                public double getStartingDTR() {
                    return startingDTR;
                }

                public double getLossPerDeathBase() {
                    return this.lossPerDeath;
                }

                public double getLossPerDeath(World world) {
                    if (this.worldDeathModifiers == null) {
                        this.worldDeathModifiers = new HashMap<>();
                    }
                    return this.lossPerDeath * this.worldDeathModifiers.getOrDefault(world.getName(), 1D).doubleValue();
                }
            }

            public class Power {
                private final double playerMin = -10.0D;
                private final double playerMax = 10.0D;
                private final double playerStarting = 0.0D;
                @Comment("Default health rate of 0.2 takes 5 minutes to recover one power")
                private final double powerPerMinute = 0.2;
                @Comment("How much is lost on death")
                private final double lossPerDeath = 4.0;
                @Comment("Does a player regenerate power while offline?")
                private final boolean regenOffline = false;
                @Comment("A player loses this much per day offline")
                private final double offlineLossPerDay = 0.0;
                @Comment("A player stops losing power from being offline once they reach this amount")
                private final double offlineLossLimit = 0.0;
                @Comment("If greater than 0, used as a cap for how much power a faction can have\nAdditional power from players beyond this acts as a \"buffer\" of sorts")
                private final double factionMax = 0.0;
                private final boolean respawnHomeFromNoPowerLossWorlds = true;
                private final Set<String> worldsNoPowerLoss = new HashSet<String>() {
                    {
                        this.add("exampleWorld");
                    }
                };
                private final boolean peacefulMembersDisablePowerLoss = true;
                private final boolean warZonePowerLoss = true;
                private final boolean wildernessPowerLoss = true;
                @Comment("Disallow joining/leaving/kicking while power is negative")
                private final boolean canLeaveWithNegativePower = true;
                @Comment("Allow a faction to be raided if they have more land than power.\n" +
                        "This will make claimed territory lose all protections\n" +
                        "  allowing factions to open chests, break blocks, etc. if they\n" +
                        "  have claimed chunks >= power.")
                private final boolean raidability = false;
                @Comment("After a player dies, how long should the faction not be able to regen power?\n" +
                        "This resets on each death but does not accumulate.\n" +
                        "Set to 0 for no freeze. Time is in seconds.")
                private final int powerFreeze = 0;

                public boolean isRaidability() {
                    return raidability;
                }

                public int getPowerFreeze() {
                    return powerFreeze;
                }

                public boolean canLeaveWithNegativePower() {
                    return canLeaveWithNegativePower;
                }

                public boolean isWarZonePowerLoss() {
                    return warZonePowerLoss;
                }

                public boolean isWildernessPowerLoss() {
                    return wildernessPowerLoss;
                }

                public double getPlayerMin() {
                    return playerMin;
                }

                public double getPlayerMax() {
                    return playerMax;
                }

                public double getPlayerStarting() {
                    return playerStarting;
                }

                public double getPowerPerMinute() {
                    return powerPerMinute;
                }

                public double getLossPerDeath() {
                    return lossPerDeath;
                }

                public boolean isRegenOffline() {
                    return regenOffline;
                }

                public double getOfflineLossPerDay() {
                    return offlineLossPerDay;
                }

                public double getOfflineLossLimit() {
                    return offlineLossLimit;
                }

                public double getFactionMax() {
                    return factionMax;
                }

                public boolean isRespawnHomeFromNoPowerLossWorlds() {
                    return respawnHomeFromNoPowerLossWorlds;
                }

                public Set<String> getWorldsNoPowerLoss() {
                    return worldsNoPowerLoss == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsNoPowerLoss);
                }

                public boolean isPeacefulMembersDisablePowerLoss() {
                    return peacefulMembersDisablePowerLoss;
                }
            }

            @Comment("Sets the mode of land/raid control")
            private final String system = "power";
            private final DTR dtr = new DTR();
            @Comment("Controls the power system of land/raid control\nSet the 'system' value to 'power' to use this system")
            private final Power power = new Power();

            public String getSystem() {
                return system;
            }

            public DTR dtr() {
                return this.dtr;
            }

            public Power power() {
                return power;
            }
        }

        public class Prefix {
            private final String admin = "***";
            private final String coleader = "**";
            private final String mod = "*";
            private final String normal = "+";
            private final String recruit = "-";

            public String getAdmin() {
                return admin;
            }

            public String getColeader() {
                return coleader;
            }

            public String getMod() {
                return mod;
            }

            public String getNormal() {
                return normal;
            }

            public String getRecruit() {
                return recruit;
            }
        }

        public class Chat {
            @Comment("Allow for players to chat only within their faction, with allies, etc.\n" +
                    "Set to false to only allow public chats through this plugin.")
            private final boolean factionOnlyChat = true;
            // Configuration on the Faction tag in chat messages.
            @Comment("If true, disables adding of faction tag so another plugin can manage this")
            private final boolean tagHandledByAnotherPlugin = false;
            private final boolean tagRelationColored = true;
            private final String tagReplaceString = "[FACTION]";
            private final String tagInsertAfterString = "";
            private final String tagInsertBeforeString = "";
            private final int tagInsertIndex = 0;
            private final boolean tagPadBefore = false;
            private final boolean tagPadAfter = true;
            private final String tagFormat = "%s\u00A7f";
            private final boolean alwaysShowChatTag = true;
            private final String factionChatFormat = "%s:\u00A7f %s";
            private final String allianceChatFormat = "\u00A7d%s:\u00A7f %s";
            private final String truceChatFormat = "\u00A75%s:\u00A7f %s";
            private final String modChatFormat = "\u00A7a%s:\u00A7f %s";
            private final String coleaderChatFormat = "\u00A7b%s:\u00A7f %s";
            private final boolean broadcastDescriptionChanges = false;
            private final boolean broadcastTagChanges = false;

            public boolean isFactionOnlyChat() {
                return factionOnlyChat;
            }

            public boolean isTagHandledByAnotherPlugin() {
                return tagHandledByAnotherPlugin;
            }

            public boolean isTagRelationColored() {
                return tagRelationColored;
            }

            public String getTagReplaceString() {
                return tagReplaceString;
            }

            public String getTagInsertAfterString() {
                return tagInsertAfterString;
            }

            public String getTagInsertBeforeString() {
                return tagInsertBeforeString;
            }

            public int getTagInsertIndex() {
                return tagInsertIndex;
            }

            public boolean isTagPadBefore() {
                return tagPadBefore;
            }

            public boolean isTagPadAfter() {
                return tagPadAfter;
            }

            public String getTagFormat() {
                return tagFormat;
            }

            public boolean isAlwaysShowChatTag() {
                return alwaysShowChatTag;
            }

            public String getFactionChatFormat() {
                return factionChatFormat;
            }

            public String getAllianceChatFormat() {
                return allianceChatFormat;
            }

            public String getTruceChatFormat() {
                return truceChatFormat;
            }

            public String getModChatFormat() {
                return modChatFormat;
            }

            public String getColeaderChatFormat() {
                return coleaderChatFormat;
            }

            public boolean isBroadcastDescriptionChanges() {
                return broadcastDescriptionChanges;
            }

            public boolean isBroadcastTagChanges() {
                return broadcastTagChanges;
            }
        }

        public class Homes {
            private final boolean enabled = true;
            private final boolean mustBeInClaimedTerritory = true;
            private final boolean teleportToOnDeath = true;
            private final boolean teleportCommandEnabled = true;
            private final boolean teleportCommandEssentialsIntegration = true;
            private final boolean teleportCommandSmokeEffectEnabled = true;
            private final float teleportCommandSmokeEffectThickness = 3f;
            private final boolean teleportAllowedFromEnemyTerritory = true;
            private final boolean teleportAllowedFromDifferentWorld = true;
            private final double teleportAllowedEnemyDistance = 32.0;
            private final boolean teleportIgnoreEnemiesIfInOwnTerritory = true;

            public boolean isEnabled() {
                return enabled;
            }

            public boolean isMustBeInClaimedTerritory() {
                return mustBeInClaimedTerritory;
            }

            public boolean isTeleportToOnDeath() {
                return teleportToOnDeath;
            }

            public boolean isTeleportCommandEnabled() {
                return teleportCommandEnabled;
            }

            public boolean isTeleportCommandEssentialsIntegration() {
                return teleportCommandEssentialsIntegration;
            }

            public boolean isTeleportCommandSmokeEffectEnabled() {
                return teleportCommandSmokeEffectEnabled;
            }

            public float getTeleportCommandSmokeEffectThickness() {
                return teleportCommandSmokeEffectThickness;
            }

            public boolean isTeleportAllowedFromEnemyTerritory() {
                return teleportAllowedFromEnemyTerritory;
            }

            public boolean isTeleportAllowedFromDifferentWorld() {
                return teleportAllowedFromDifferentWorld;
            }

            public double getTeleportAllowedEnemyDistance() {
                return teleportAllowedEnemyDistance;
            }

            public boolean isTeleportIgnoreEnemiesIfInOwnTerritory() {
                return teleportIgnoreEnemiesIfInOwnTerritory;
            }
        }

        public class MaxRelations {
            private final boolean enabled = false;
            private final int ally = 10;
            private final int truce = 10;
            private final int neutral = -1;
            private final int enemy = 10;

            public boolean isEnabled() {
                return enabled;
            }

            public int getAlly() {
                return ally;
            }

            public int getTruce() {
                return truce;
            }

            public int getNeutral() {
                return neutral;
            }

            public int getEnemy() {
                return enemy;
            }
        }

        public class PVP {
            private final boolean disablePVPBetweenNeutralFactions = false;
            private final boolean disablePVPForFactionlessPlayers = false;
            private final boolean enablePVPAgainstFactionlessInAttackersLand = false;
            private final boolean disablePeacefulPVPInWarzone = true;
            private final int noPVPDamageToOthersForXSecondsAfterLogin = 3;
            private final Set<String> worldsIgnorePvP = new HashSet<String>() {
                {
                    this.add("exampleWorldName");
                }
            };

            public boolean isDisablePVPBetweenNeutralFactions() {
                return disablePVPBetweenNeutralFactions;
            }

            public boolean isDisablePVPForFactionlessPlayers() {
                return disablePVPForFactionlessPlayers;
            }

            public boolean isDisablePeacefulPVPInWarzone() {
                return disablePeacefulPVPInWarzone;
            }

            public boolean isEnablePVPAgainstFactionlessInAttackersLand() {
                return enablePVPAgainstFactionlessInAttackersLand;
            }

            public int getNoPVPDamageToOthersForXSecondsAfterLogin() {
                return noPVPDamageToOthersForXSecondsAfterLogin;
            }

            public Set<String> getWorldsIgnorePvP() {
                return worldsIgnorePvP == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsIgnorePvP);
            }
        }

        public class SpecialCase {
            private final boolean peacefulTerritoryDisablePVP = true;
            private final boolean peacefulTerritoryDisableMonsters = false;
            private final boolean peacefulTerritoryDisableBoom = false;
            private final boolean permanentFactionsDisableLeaderPromotion = false;
            @Comment("Material names of things whose placement is ignored in faction territory")
            private Set<String> ignoreBuildMaterials = new HashSet<String>() {
                {
                    this.add("exampleMaterial");
                }
            };
            @WipeOnReload
            private transient Set<Material> ignoreBuildMaterialsMat;

            public Set<Material> getIgnoreBuildMaterials() {
                if (ignoreBuildMaterialsMat == null) {
                    ignoreBuildMaterialsMat = EnumSet.noneOf(Material.class);
                    ignoreBuildMaterials.forEach(m -> ignoreBuildMaterialsMat.add(FactionMaterial.from(m).get()));
                    ignoreBuildMaterialsMat.remove(Material.AIR);
                    ignoreBuildMaterials = Collections.unmodifiableSet(ignoreBuildMaterials);
                }
                return ignoreBuildMaterialsMat;
            }

            public boolean isPeacefulTerritoryDisablePVP() {
                return peacefulTerritoryDisablePVP;
            }

            public boolean isPeacefulTerritoryDisableMonsters() {
                return peacefulTerritoryDisableMonsters;
            }

            public boolean isPeacefulTerritoryDisableBoom() {
                return peacefulTerritoryDisableBoom;
            }

            public boolean isPermanentFactionsDisableLeaderPromotion() {
                return permanentFactionsDisableLeaderPromotion;
            }
        }

        public class Portals {
            @Comment("If true, portals will be limited to the minimum relation below")
            private final boolean limit = false;
            @Comment("What should the minimum relation be to create a portal in territory?\n" +
                    "Goes in the order of: ENEMY, NEUTRAL, ALLY, MEMBER.\n" +
                    "Minimum relation allows that and all listed to the right to create portals.\n" +
                    "Example: put ALLY to allow ALLY and MEMBER to be able to create portals.\n" +
                    "If typed incorrectly, defaults to NEUTRAL.")
            private final String minimumRelation = "MEMBER";

            public boolean isLimit() {
                return limit;
            }

            public String getMinimumRelation() {
                return minimumRelation;
            }
        }

        public class Claims {
            private final boolean optimizeClaiming = true;
            private final boolean mustBeConnected = false;
            private final boolean canBeUnconnectedIfOwnedByOtherFaction = true;
            private final int requireMinFactionMembers = 1;
            private final int landsMax = 0;
            private final int lineClaimLimit = 5;
            private final int fillClaimMaxClaims = 25;
            private final int fillClaimMaxDistance = 5;
            @Comment("If someone is doing a radius claim and the process fails to claim land this many times in a row, it will exit")
            private final int radiusClaimFailureLimit = 9;
            private final Set<String> worldsNoClaiming = new HashSet<String>() {
                {
                    this.add("exampleWorldName");
                }
            };
            @Comment("Buffer Zone is an chunk area required between claims of different Factions.\n" +
                    "This is default to 0 and has always been that way. Meaning Factions can have\n" +
                    "  claims that border each other.\n" +
                    "If this is set to 3, then Factions need to have 3 chunks between their claim\n" +
                    "  and another Faction's claim.\n" +
                    "It's recommended to keep this pretty low as the radius check could be a\n" +
                    "  heavy operation if set to a large number.\n" +
                    "If this is set to 0, we won't even bother checking which is how Factions has\n" +
                    "  always been.")
            private final int bufferZone = 0;
            @Comment("Should we allow Factions to over claim if they are raidable?\n" +
                    "This has always been true, allowing factions to over claim others.")
            private final boolean allowOverClaim = true;
            @Comment("If true (and allowOverClaim is true, claiming over another faction's land will ignore buffer zone settings.")
            private final boolean allowOverClaimIgnoringBuffer = false;

            public boolean isAllowOverClaim() {
                return allowOverClaim;
            }

            public boolean isAllowOverClaimAndIgnoringBuffer() {
                return allowOverClaim && allowOverClaimIgnoringBuffer;
            }

            public int getBufferZone() {
                return bufferZone;
            }

            public boolean shouldOptimizeClaiming() {
                return optimizeClaiming;
            }

            public boolean isMustBeConnected() {
                return mustBeConnected;
            }

            public boolean isCanBeUnconnectedIfOwnedByOtherFaction() {
                return canBeUnconnectedIfOwnedByOtherFaction;
            }

            public int getRequireMinFactionMembers() {
                return requireMinFactionMembers;
            }

            public int getLandsMax() {
                return landsMax;
            }

            public int getFillClaimMaxClaims() {
                return fillClaimMaxClaims;
            }

            public int getFillClaimMaxDistance() {
                return fillClaimMaxDistance;
            }

            public int getLineClaimLimit() {
                return lineClaimLimit;
            }

            public int getRadiusClaimFailureLimit() {
                return radiusClaimFailureLimit;
            }

            public Set<String> getWorldsNoClaiming() {
                return this.worldsNoClaiming;
            }
        }

        public class Protection {
            @Comment("Commands which will be prevented if the player is a member of a permanent faction")
            private final Set<String> permanentFactionMemberDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };

            @Comment("Commands which will be prevented when in claimed territory of a neutral faction")
            private final Set<String> territoryNeutralDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
            @Comment("Commands which will be prevented when in claimed territory of an enemy faction")
            private final Set<String> territoryEnemyDenyCommands = new HashSet<String>() {
                {
                    this.add("home");
                    this.add("sethome");
                    this.add("spawn");
                    this.add("tpahere");
                    this.add("tpaccept");
                    this.add("tpa");
                }
            };
            @Comment("Commands which will be prevented when in claimed territory of an ally faction")
            private final Set<String> territoryAllyDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
            @Comment("Commands which will be prevented when in warzone")
            private final Set<String> warzoneDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
            @Comment("Commands which will be prevented when in wilderness")
            private final Set<String> wildernessDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };

            private final boolean territoryBlockCreepers = false;
            private final boolean territoryBlockCreepersWhenOffline = false;
            private final boolean territoryBlockFireballs = false;
            private final boolean territoryBlockFireballsWhenOffline = false;
            private final boolean territoryBlockTNT = false;
            private final boolean territoryBlockTNTWhenOffline = false;
            private final boolean territoryBlockOtherExplosions = false;
            private final boolean territoryBlockOtherExplosionsWhenOffline = false;
            private final boolean territoryDenyEndermanBlocks = true;
            private final boolean territoryDenyEndermanBlocksWhenOffline = true;

            private final boolean safeZoneDenyBuild = true;
            private final boolean safeZoneDenyUsage = true;
            private final boolean safeZoneBlockTNT = true;
            private final boolean safeZoneBlockOtherExplosions = true;
            private final boolean safeZonePreventAllDamageToPlayers = false;
            private final boolean safeZoneDenyEndermanBlocks = true;

            private final boolean warZoneDenyBuild = true;
            private final boolean warZoneDenyUsage = true;
            private final boolean warZoneBlockCreepers = true;
            private final boolean warZoneBlockFireballs = true;
            private final boolean warZoneBlockTNT = true;
            private final boolean warZoneBlockOtherExplosions = true;
            private final boolean warZoneFriendlyFire = false;
            private final boolean warZoneDenyEndermanBlocks = true;
            private final boolean warZonePreventMonsterSpawns = false;

            private final boolean wildernessDenyBuild = false;
            private final boolean wildernessDenyUsage = false;
            private final boolean wildernessBlockCreepers = false;
            private final boolean wildernessBlockFireballs = false;
            private final boolean wildernessBlockTNT = false;
            private final boolean wildernessBlockOtherExplosions = false;
            private final boolean wildernessDenyEndermanBlocks = false;

            private final boolean pistonProtectionThroughDenyBuild = true;

            private final Set<String> territoryDenyUsageMaterials = new HashSet<>();
            private final Set<String> territoryDenyUsageMaterialsWhenOffline = new HashSet<>();
            @WipeOnReload
            private transient Set<Material> territoryDenyUsageMaterialsMat;
            @WipeOnReload
            private transient Set<Material> territoryDenyUsageMaterialsWhenOfflineMat;

            @Comment("Mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections")
            private final Set<String> playersWhoBypassAllProtection = new HashSet<String>() {
                {
                    this.add("example-player-name");
                }
            };
            private final Set<String> worldsNoWildernessProtection = new HashSet<String>() {
                {
                    this.add("exampleWorld");
                }
            };

            private Protection() {
                protectUsage("FIRE_CHARGE");
                protectUsage("FLINT_AND_STEEL");
                protectUsage("BUCKET");
                protectUsage("WATER_BUCKET");
                protectUsage("LAVA_BUCKET");
            }

            private void protectUsage(String material) {
                territoryDenyUsageMaterials.add(material);
                territoryDenyUsageMaterialsWhenOffline.add(material);
            }

            public Set<String> getPermanentFactionMemberDenyCommands() {
                return permanentFactionMemberDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(permanentFactionMemberDenyCommands);
            }

            public Set<String> getTerritoryNeutralDenyCommands() {
                return territoryNeutralDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryNeutralDenyCommands);
            }

            public Set<String> getTerritoryEnemyDenyCommands() {
                return territoryEnemyDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryEnemyDenyCommands);
            }

            public Set<String> getTerritoryAllyDenyCommands() {
                return territoryAllyDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryAllyDenyCommands);
            }

            public Set<String> getWarzoneDenyCommands() {
                return warzoneDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(warzoneDenyCommands);
            }

            public Set<String> getWildernessDenyCommands() {
                return wildernessDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(wildernessDenyCommands);
            }

            public boolean isTerritoryBlockCreepers() {
                return territoryBlockCreepers;
            }

            public boolean isTerritoryBlockCreepersWhenOffline() {
                return territoryBlockCreepersWhenOffline;
            }

            public boolean isTerritoryBlockFireballs() {
                return territoryBlockFireballs;
            }

            public boolean isTerritoryBlockFireballsWhenOffline() {
                return territoryBlockFireballsWhenOffline;
            }

            public boolean isTerritoryBlockTNT() {
                return territoryBlockTNT;
            }

            public boolean isTerritoryBlockTNTWhenOffline() {
                return territoryBlockTNTWhenOffline;
            }

            public boolean isTerritoryDenyEndermanBlocks() {
                return territoryDenyEndermanBlocks;
            }

            public boolean isTerritoryDenyEndermanBlocksWhenOffline() {
                return territoryDenyEndermanBlocksWhenOffline;
            }

            public boolean isSafeZoneDenyBuild() {
                return safeZoneDenyBuild;
            }

            public boolean isSafeZoneDenyUsage() {
                return safeZoneDenyUsage;
            }

            public boolean isSafeZoneBlockTNT() {
                return safeZoneBlockTNT;
            }

            public boolean isSafeZonePreventAllDamageToPlayers() {
                return safeZonePreventAllDamageToPlayers;
            }

            public boolean isSafeZoneDenyEndermanBlocks() {
                return safeZoneDenyEndermanBlocks;
            }

            public boolean isWarZoneDenyBuild() {
                return warZoneDenyBuild;
            }

            public boolean isWarZoneDenyUsage() {
                return warZoneDenyUsage;
            }

            public boolean isWarZoneBlockCreepers() {
                return warZoneBlockCreepers;
            }

            public boolean isWarZoneBlockFireballs() {
                return warZoneBlockFireballs;
            }

            public boolean isWarZoneBlockTNT() {
                return warZoneBlockTNT;
            }

            public boolean isWarZoneFriendlyFire() {
                return warZoneFriendlyFire;
            }

            public boolean isWarZoneDenyEndermanBlocks() {
                return warZoneDenyEndermanBlocks;
            }

            public boolean isWarZonePreventMonsterSpawns() {
                return warZonePreventMonsterSpawns;
            }

            public boolean isWildernessDenyBuild() {
                return wildernessDenyBuild;
            }

            public boolean isWildernessDenyUsage() {
                return wildernessDenyUsage;
            }

            public boolean isWildernessBlockCreepers() {
                return wildernessBlockCreepers;
            }

            public boolean isWildernessBlockFireballs() {
                return wildernessBlockFireballs;
            }

            public boolean isWildernessBlockTNT() {
                return wildernessBlockTNT;
            }

            public boolean isWildernessDenyEndermanBlocks() {
                return wildernessDenyEndermanBlocks;
            }

            public boolean isPistonProtectionThroughDenyBuild() {
                return pistonProtectionThroughDenyBuild;
            }

            public boolean isTerritoryBlockOtherExplosions() {
                return territoryBlockOtherExplosions;
            }

            public boolean isTerritoryBlockOtherExplosionsWhenOffline() {
                return territoryBlockOtherExplosionsWhenOffline;
            }

            public boolean isSafeZoneBlockOtherExplosions() {
                return safeZoneBlockOtherExplosions;
            }

            public boolean isWarZoneBlockOtherExplosions() {
                return warZoneBlockOtherExplosions;
            }

            public boolean isWildernessBlockOtherExplosions() {
                return wildernessBlockOtherExplosions;
            }

            public Set<Material> getTerritoryDenyUsageMaterials() {
                if (territoryDenyUsageMaterialsMat == null) {
                    territoryDenyUsageMaterialsMat = EnumSet.noneOf(Material.class);
                    territoryDenyUsageMaterials.forEach(m -> territoryDenyUsageMaterialsMat.add(FactionMaterial.from(m).get()));
                    territoryDenyUsageMaterialsMat.remove(Material.AIR);
                    territoryDenyUsageMaterialsMat = Collections.unmodifiableSet(territoryDenyUsageMaterialsMat);
                }
                return territoryDenyUsageMaterialsMat;
            }

            public Set<Material> getTerritoryDenyUsageMaterialsWhenOffline() {
                if (territoryDenyUsageMaterialsWhenOfflineMat == null) {
                    territoryDenyUsageMaterialsWhenOfflineMat = EnumSet.noneOf(Material.class);
                    territoryDenyUsageMaterialsWhenOffline.forEach(m -> territoryDenyUsageMaterialsWhenOfflineMat.add(FactionMaterial.from(m).get()));
                    territoryDenyUsageMaterialsWhenOfflineMat.remove(Material.AIR);
                    territoryDenyUsageMaterialsWhenOfflineMat = Collections.unmodifiableSet(territoryDenyUsageMaterialsWhenOfflineMat);
                }
                return territoryDenyUsageMaterialsWhenOfflineMat;
            }

            public Set<String> getPlayersWhoBypassAllProtection() {
                return playersWhoBypassAllProtection == null ? Collections.emptySet() : this.playersWhoBypassAllProtection;
            }

            public Set<String> getWorldsNoWildernessProtection() {
                return worldsNoWildernessProtection == null ? Collections.emptySet() : this.worldsNoWildernessProtection;
            }
        }

        public class OwnedArea {
            private final boolean enabled = true;
            private final int limitPerFaction = 0;
            private final boolean moderatorsBypass = true;
            private final boolean denyBuild = true;
            private final boolean painBuild = false;
            private final boolean protectMaterials = true;
            private final boolean denyUsage = true;

            private final boolean messageOnBorder = true;
            private final boolean messageInsideTerritory = true;
            private final boolean messageByChunk = false;

            public boolean isEnabled() {
                return enabled;
            }

            public int getLimitPerFaction() {
                return limitPerFaction;
            }

            public boolean isModeratorsBypass() {
                return moderatorsBypass;
            }

            public boolean isDenyBuild() {
                return denyBuild;
            }

            public boolean isPainBuild() {
                return painBuild;
            }

            public boolean isProtectMaterials() {
                return protectMaterials;
            }

            public boolean isDenyUsage() {
                return denyUsage;
            }

            public boolean isMessageOnBorder() {
                return messageOnBorder;
            }

            public boolean isMessageInsideTerritory() {
                return messageInsideTerritory;
            }

            public boolean isMessageByChunk() {
                return messageByChunk;
            }
        }

        public class Other {
            private final boolean allowMultipleColeaders = false;

            @Comment("Minimum faction tag length")
            private final int tagLengthMin = 3;
            @Comment("Maximum faction tag length")
            private final int tagLengthMax = 10;
            private final boolean tagForceUpperCase = false;

            private final boolean newFactionsDefaultOpen = false;

            @Comment("When faction membership hits this limit, players will no longer be able to join using /f join; default is 0, no limit")
            private final int factionMemberLimit = 0;

            @Comment("What faction ID to start new players in when they first join the server; default is 0, \"no faction\"")
            private final int newPlayerStartingFactionID = 0;

            private final double saveToFileEveryXMinutes = 30.0;

            private final double autoLeaveAfterDaysOfInactivity = 10.0;
            private final double autoLeaveRoutineRunsEveryXMinutes = 5.0;
            private final int autoLeaveRoutineMaxMillisecondsPerTick = 5;  // 1 server tick is roughly 50ms, so default max 10% of a tick
            private final boolean removePlayerDataWhenBanned = true;
            private final boolean autoLeaveDeleteFPlayerData = true; // Let them just remove player from Faction.
            private final double considerFactionsReallyOfflineAfterXMinutes = 0.0;
            private final int actionDeniedPainAmount = 1;

            @Comment("If enabled, perms can be managed separately for when the faction is offline")
            private final boolean separateOfflinePerms = false;

            @Comment("Should we delete player homes that they set via Essentials when they leave a Faction\n" +
                    "if they have homes set in that Faction's territory?")
            private final boolean deleteEssentialsHomes = true;

            @Comment("Should the player be teleported to spawn if they attempt to logout on enemy territory?")
            private final boolean spawnTeleportIfEnemyTerritory = false;

            @Comment("Default Relation allows you to change the default relation for Factions.\n" +
                    "Example usage would be so people can't leave then make a new Faction while Raiding\n" +
                    "  in order to be able to execute commands if the default relation is neutral.")
            private final String defaultRelation = "neutral";

            @Comment("Default role of a player when joining a faction. Can be customized by faction leader\n" +
                    "with /f defaultrole\n" +
                    "Options: coleader, moderator, member, recruit\n" +
                    "Defaults to member if set incorrectly")
            private final String defaultRole = "member";
            private transient Role defaultRoleRole;

            @Comment("If true, disables pistons entirely within faction territory.\n" +
                    "Prevents flying piston machines in faction territory.")
            private final boolean disablePistonsInTerritory = false;

            @Comment("If true, water will not flow onto redstone which normally causes it to break.")
            private final boolean disableWaterFlowOnRedstone = true;

            @Comment("Any faction names CONTAINING any of these items will be disallowed")
            private final List<String> nameBlacklist = new ArrayList<String>() {
                {
                    this.add("blockedwordhere");
                    this.add("anotherblockedthinghere");
                }
            };

            public List<String> getNameBlacklist() {
                return nameBlacklist == null ? Collections.emptyList() : Collections.unmodifiableList(this.nameBlacklist);
            }

            public boolean isDisablePistonsInTerritory() {
                return disablePistonsInTerritory;
            }

            public boolean isDisableWaterFlowOnRedstone() {
                return disableWaterFlowOnRedstone;
            }

            public boolean isDeleteEssentialsHomes() {
                return deleteEssentialsHomes;
            }

            public boolean isSpawnTeleportIfEnemyTerritory() {
                return spawnTeleportIfEnemyTerritory;
            }

            public String getDefaultRelation() {
                return defaultRelation;
            }

            public Role getDefaultRole() {
                if (defaultRoleRole == null) {
                    if ((defaultRoleRole = Role.fromString(defaultRole)) == null) {
                        defaultRoleRole = Role.NORMAL;
                    }
                }
                return defaultRoleRole;
            }

            public boolean isSeparateOfflinePerms() {
                return separateOfflinePerms;
            }

            public boolean isAllowMultipleColeaders() {
                return allowMultipleColeaders;
            }

            public int getTagLengthMin() {
                return tagLengthMin;
            }

            public int getTagLengthMax() {
                return tagLengthMax;
            }

            public boolean isTagForceUpperCase() {
                return tagForceUpperCase;
            }

            public boolean isNewFactionsDefaultOpen() {
                return newFactionsDefaultOpen;
            }

            public int getFactionMemberLimit() {
                return factionMemberLimit;
            }

            public int getNewPlayerStartingFactionID() {
                return newPlayerStartingFactionID;
            }

            public double getSaveToFileEveryXMinutes() {
                return saveToFileEveryXMinutes;
            }

            public double getAutoLeaveAfterDaysOfInactivity() {
                return autoLeaveAfterDaysOfInactivity;
            }

            public double getAutoLeaveRoutineRunsEveryXMinutes() {
                return autoLeaveRoutineRunsEveryXMinutes;
            }

            public int getAutoLeaveRoutineMaxMillisecondsPerTick() {
                return autoLeaveRoutineMaxMillisecondsPerTick;
            }

            public boolean isRemovePlayerDataWhenBanned() {
                return removePlayerDataWhenBanned;
            }

            public boolean isAutoLeaveDeleteFPlayerData() {
                return autoLeaveDeleteFPlayerData;
            }

            public double getConsiderFactionsReallyOfflineAfterXMinutes() {
                return considerFactionsReallyOfflineAfterXMinutes;
            }

            public int getActionDeniedPainAmount() {
                return actionDeniedPainAmount;
            }
        }

        public class EnterTitles {
            private final boolean enabled = true;
            private final int fadeIn = 10;
            private final int stay = 70;
            private final int fadeOut = 20;
            private final boolean alsoShowChat = false;
            private final String title = "{faction-relation-color}{faction}";
            private final String subtitle = "&7{description}";

            public boolean isEnabled() {
                return enabled;
            }

            public int getFadeIn() {
                return fadeIn;
            }

            public int getStay() {
                return stay;
            }

            public int getFadeOut() {
                return fadeOut;
            }

            public boolean isAlsoShowChat() {
                return alsoShowChat;
            }

            public String getTitle() {
                return title;
            }

            public String getSubtitle() {
                return subtitle;
            }
        }

        public class EnterActionBars {
            private final boolean enabled = true;
            private final int stay = 70;
            private final boolean alsoShowChat = false;
            private final String message = "&eNow entering: {faction-relation-color}{faction} - &7{description}";

            public boolean isEnabled() {
                return enabled;
            }

            public int getStay() {
                return stay;
            }

            public boolean isAlsoShowChat() {
                return alsoShowChat;
            }

            public String getMessage() {
                return message;
            }
        }

        private final Chat chat = new Chat();
        private final Homes homes = new Homes();
        @Comment("Limits factions to having a max number of each relation.\n" +
                "Setting to 0 means none allowed. -1 for disabled.\n" +
                "This will have no effect on default or existing relations, only when relations are changed.\n" +
                "It is advised that you set the default relation to -1 so they can always go back to that.\n" +
                "Otherwise Factions could be stuck with not being able to unenemy other Factions.")
        private final MaxRelations maxRelations = new MaxRelations();
        private final PVP pvp = new PVP();
        private final SpecialCase specialCase = new SpecialCase();
        private final Claims claims = new Claims();
        @Comment("Do you want to limit portal creation?")
        private final Portals portals = new Portals();
        private final Protection protection = new Protection();
        @Comment("For claimed areas where further faction-member ownership can be defined")
        private final OwnedArea ownedArea = new OwnedArea();
        @Comment("Displayed prefixes for different roles within a faction")
        private final Prefix prefixes = new Prefix();
        private final LandRaidControl landRaidControl = new LandRaidControl();
        @Comment("Remaining settings not categorized")
        private final Other other = new Other();
        @Comment("Should we send titles when players enter Factions? Durations are in ticks (20 ticks every second)")
        private final EnterTitles enterTitles = new EnterTitles();
        @Comment("Should we send actionbars when players enter Factions? Durations are in ticks (20 ticks every second)")
        private final EnterActionBars enterActionBars = new EnterActionBars();

        public EnterTitles enterTitles() {
            return enterTitles;
        }

        public EnterActionBars enterActionBars() {
            return enterActionBars;
        }

        public Chat chat() {
            return chat;
        }

        public Homes homes() {
            return homes;
        }

        public MaxRelations maxRelations() {
            return maxRelations;
        }

        public PVP pvp() {
            return pvp;
        }

        public SpecialCase specialCase() {
            return specialCase;
        }

        public Claims claims() {
            return claims;
        }

        public Portals portals() {
            return portals;
        }

        public Protection protection() {
            return protection;
        }

        public Other other() {
            return other;
        }

        public OwnedArea ownedArea() {
            return ownedArea;
        }

        public Prefix prefixes() {
            return prefixes;
        }

        public LandRaidControl landRaidControl() {
            return landRaidControl;
        }
    }

    public class Logging {
        private final boolean factionCreate = true;
        private final boolean factionDisband = true;
        private final boolean factionJoin = true;
        private final boolean factionKick = true;
        private final boolean factionLeave = true;
        private final boolean landClaims = true;
        private final boolean landUnclaims = true;
        private final boolean moneyTransactions = true;
        private final boolean playerCommands = true;

        public boolean isFactionCreate() {
            return factionCreate;
        }

        public boolean isFactionDisband() {
            return factionDisband;
        }

        public boolean isFactionJoin() {
            return factionJoin;
        }

        public boolean isFactionKick() {
            return factionKick;
        }

        public boolean isFactionLeave() {
            return factionLeave;
        }

        public boolean isLandClaims() {
            return landClaims;
        }

        public boolean isLandUnclaims() {
            return landUnclaims;
        }

        public boolean isMoneyTransactions() {
            return moneyTransactions;
        }

        public boolean isPlayerCommands() {
            return playerCommands;
        }
    }

    public class Exploits {
        private final boolean obsidianGenerators = true;
        private final boolean enderPearlClipping = true;
        private final boolean interactionSpam = true;
        private final boolean tntWaterlog = false;
        private final boolean liquidFlow = false;
        private final boolean preventDuping = true;

        public boolean isObsidianGenerators() {
            return obsidianGenerators;
        }

        public boolean isEnderPearlClipping() {
            return enderPearlClipping;
        }

        public boolean isInteractionSpam() {
            return interactionSpam;
        }

        public boolean isTntWaterlog() {
            return tntWaterlog;
        }

        public boolean isLiquidFlow() {
            return liquidFlow;
        }

        public boolean doPreventDuping() {
            return preventDuping;
        }
    }

    public class Economy {
        @Comment("Must be true for any economy features")
        private final boolean enabled = false;
        private final String universeAccount = "";
        private final double costClaimWilderness = 30.0;
        private final double costClaimFromFactionBonus = 30.0;
        private final double overclaimRewardMultiplier = 0.0;
        private final double claimAdditionalMultiplier = 0.5;
        private final double claimRefundMultiplier = 0.7;
        private final double claimUnconnectedFee = 0.0;
        private final double costCreate = 100.0;
        private final double costOwner = 15.0;
        private final double costSethome = 30.0;
        private final double costDelhome = 30.0;
        private final double costJoin = 0.0;
        private final double costLeave = 0.0;
        private final double costKick = 0.0;
        private final double costInvite = 0.0;
        private final double costHome = 0.0;
        private final double costTag = 0.0;
        private final double costDesc = 0.0;
        private final double costTitle = 0.0;
        private final double costList = 0.0;
        private final double costMap = 0.0;
        private final double costPower = 0.0;
        private final double costShow = 0.0;
        private final double costStuck = 0.0;
        private final double costDTR = 0.0;
        private final double costOpen = 0.0;
        private final double costAlly = 0.0;
        private final double costTruce = 0.0;
        private final double costEnemy = 0.0;
        private final double costNeutral = 0.0;
        private final double costNoBoom = 0.0;
        // Calculate if enabled
        private final double costWarp = 0.0;
        private final double costSetWarp = 0.0;
        private final double costDelWarp = 0.0;

        @Comment("Faction banks, to pay for land claiming and other costs instead of individuals paying for them")
        private final boolean bankEnabled = true;
        @Comment("Have to be at least moderator to withdraw or pay money to another faction")
        private final boolean bankMembersCanWithdraw = false;
        @Comment("The faction pays for faction command costs, such as sethome")
        private final boolean bankFactionPaysCosts = true;
        @Comment("The faction pays for land claiming costs.")
        private final boolean bankFactionPaysLandCosts = true;

        public boolean isEnabled() {
            return enabled;
        }

        public double getCostDTR() {
            return costDTR;
        }

        public String getUniverseAccount() {
            return universeAccount;
        }

        public double getCostClaimWilderness() {
            return costClaimWilderness;
        }

        public double getCostClaimFromFactionBonus() {
            return costClaimFromFactionBonus;
        }

        public double getOverclaimRewardMultiplier() {
            return overclaimRewardMultiplier;
        }

        public double getClaimAdditionalMultiplier() {
            return claimAdditionalMultiplier;
        }

        public double getClaimRefundMultiplier() {
            return claimRefundMultiplier;
        }

        public double getClaimUnconnectedFee() {
            return claimUnconnectedFee;
        }

        public double getCostCreate() {
            return costCreate;
        }

        public double getCostOwner() {
            return costOwner;
        }

        public double getCostSethome() {
            return costSethome;
        }

        public double getCostDelhome() {
            return costDelhome;
        }

        public double getCostJoin() {
            return costJoin;
        }

        public double getCostLeave() {
            return costLeave;
        }

        public double getCostKick() {
            return costKick;
        }

        public double getCostInvite() {
            return costInvite;
        }

        public double getCostHome() {
            return costHome;
        }

        public double getCostTag() {
            return costTag;
        }

        public double getCostDesc() {
            return costDesc;
        }

        public double getCostTitle() {
            return costTitle;
        }

        public double getCostList() {
            return costList;
        }

        public double getCostMap() {
            return costMap;
        }

        public double getCostPower() {
            return costPower;
        }

        public double getCostShow() {
            return costShow;
        }

        public double getCostStuck() {
            return costStuck;
        }

        public double getCostOpen() {
            return costOpen;
        }

        public double getCostAlly() {
            return costAlly;
        }

        public double getCostTruce() {
            return costTruce;
        }

        public double getCostEnemy() {
            return costEnemy;
        }

        public double getCostNeutral() {
            return costNeutral;
        }

        public double getCostNoBoom() {
            return costNoBoom;
        }

        public double getCostWarp() {
            return costWarp;
        }

        public double getCostSetWarp() {
            return costSetWarp;
        }

        public double getCostDelWarp() {
            return costDelWarp;
        }

        public boolean isBankEnabled() {
            return bankEnabled;
        }

        public boolean isBankMembersCanWithdraw() {
            return bankMembersCanWithdraw;
        }

        public boolean isBankFactionPaysCosts() {
            return bankFactionPaysCosts;
        }

        public boolean isBankFactionPaysLandCosts() {
            return bankFactionPaysLandCosts;
        }
    }

    public class MapSettings {
        private final int height = 17;
        private final int width = 49;
        private final boolean showFactionKey = true;
        private final boolean showNeutralFactionsOnMap = true;
        private final boolean showEnemyFactions = true;
        private final boolean showTruceFactions = true;

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean isShowFactionKey() {
            return showFactionKey;
        }

        public boolean isShowNeutralFactionsOnMap() {
            return showNeutralFactionsOnMap;
        }

        public boolean isShowEnemyFactions() {
            return showEnemyFactions;
        }

        public boolean isShowTruceFactions() {
            return showTruceFactions;
        }
    }

    public class Data {
        public class Json {
            @Comment("If true, data files will be stored without extra whitespace and linebreaks.\n" +
                    "This becomes less readable, but can cut storage use in half.")
            private final boolean efficientStorage = false;

            public boolean useEfficientStorage() {
                return efficientStorage;
            }
        }

        @Comment("Presently, the only option is JSON.")
        private final String storage = "JSON";
        private final Json json = new Json();

        public Json json() {
            return json;
        }
    }

    public class RestrictWorlds {
        @Comment("If true, Factions will only function on certain worlds")
        private final boolean restrictWorlds = false;
        @Comment("If restrictWorlds is true, this setting determines if the world list below is a whitelist or blacklist.\n" +
                "True for whitelist, false for blacklist.")
        private final boolean whitelist = true;
        private final Set<String> worldList = new HashSet<String>() {
            {
                this.add("exampleWorld");
            }
        };

        public boolean isRestrictWorlds() {
            return restrictWorlds;
        }

        public boolean isWhitelist() {
            return whitelist;
        }

        public Set<String> getWorldList() {
            return worldList == null ? Collections.emptySet() : Collections.unmodifiableSet(worldList);
        }
    }

    public class Scoreboard {
        public class Constant {
            private final boolean enabled = false;
            @Comment("Can use any placeholders, but does not update once set")
            private final String title = "Faction Status";
            @Comment("If true, show faction prefixes on nametags and in tab list if scoreboard is enabled")
            private final boolean prefixes = true;
            @Comment("Set the length limit for prefixes.\n" +
                    "If 0, will use a sane default for your Minecraft version (16 for pre-1.13, 32 for 1.13+).")
            private final int prefixLength = 0;
            private final List<String> content = new ArrayList<String>() {
                {
                    this.add("&6Your Faction");
                    this.add("{faction}");
                    this.add("&3Your Power");
                    this.add("{power}");
                    this.add("&aBalance");
                    this.add("${balance}");
                }
            };
            private final boolean factionlessEnabled = false;
            private final List<String> factionlessContent = new ArrayList<String>() {
                {
                    this.add("Make a new Faction");
                    this.add("Use /f create");
                }
            };
            private final String factionlessTitle = "Status";

            public boolean isEnabled() {
                return enabled;
            }

            public String getTitle() {
                return title;
            }

            public boolean isPrefixes() {
                return prefixes;
            }

            public int getPrefixLength() {
                return prefixLength < 1 ? (FactionsPlugin.getInstance().getMCVersion().isBefore(MinecraftVersions.v1_13) ? 16 : 32) : prefixLength;
            }

            public List<String> getContent() {
                return content != null ? Collections.unmodifiableList(content) : Collections.emptyList();
            }

            public boolean isFactionlessEnabled() {
                return factionlessEnabled;
            }

            public List<String> getFactionlessContent() {
                return factionlessContent != null ? Collections.unmodifiableList(factionlessContent) : Collections.emptyList();
            }

            public String getFactionlessTitle() {
                return factionlessTitle;
            }
        }

        public class Info {
            @Comment("send faction change message as well when scoreboard is up?")
            private final boolean alsoSendChat = true;
            @Comment("How long do we want scoreboards to stay")
            private final int expiration = 7;
            private final boolean enabled = false;
            @Comment("Supports placeholders")
            private final List<String> content = new ArrayList<String>() {
                {
                    this.add("&6Power");
                    this.add("{power}");
                    this.add("&3Members");
                    this.add("{online}/{members}");
                    this.add("&4Leader");
                    this.add("{leader}");
                    this.add("&bTerritory");
                    this.add("{chunks}");
                }
            };
            private final String title = "{faction-relation-color}{faction}";

            public boolean isAlsoSendChat() {
                return alsoSendChat;
            }

            public int getExpiration() {
                return expiration;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public List<String> getContent() {
                return content != null ? Collections.unmodifiableList(content) : Collections.emptyList();
            }

            public String getTitle() {
                return title;
            }
        }

        @Comment("Constant scoreboard stays around all the time, displaying status info.\n" +
                "Also, if prefixes are enabled while it is enabled, will show prefixes on nametags and tab")
        private final Constant constant = new Constant();
        @Comment("Info scoreboard is displayed when a player walks into a new Faction's territory.\n" +
                "Scoreboard disappears after <expiration> seconds.")
        private final Info info = new Info();

        public Constant constant() {
            return constant;
        }

        public Info info() {
            return info;
        }
    }

    public class LWC {
        private final boolean enabled = true;
        private final boolean resetLocksOnUnclaim = false;
        private final boolean resetLocksOnCapture = false;

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isResetLocksOnUnclaim() {
            return resetLocksOnUnclaim;
        }

        public boolean isResetLocksOnCapture() {
            return resetLocksOnCapture;
        }
    }

    public class PlayerVaults {
        @Comment("The {0} is for the faction id")
        private final String vaultPrefix = "faction-{0}";
        private final int defaultMaxVaults = 0;

        public String getVaultPrefix() {
            return vaultPrefix;
        }

        public int getDefaultMaxVaults() {
            return defaultMaxVaults;
        }
    }

    public class WorldGuard {
        private final boolean checking = false;
        private final boolean buildPriority = false;

        public boolean isChecking() {
            return checking;
        }

        public boolean isBuildPriority() {
            return buildPriority;
        }
    }

    public class WorldBorder {
        @Comment("WorldBorder support\n" +
                "This is for Minecraft's built-in command. To get your current border: /minecraft:worldborder get\n" +
                "A buffer of 0 means faction claims can go right up to the border of the world.\n" +
                "The buffer is in chunks, so 1 as a buffer means an entire chunk of buffer between\n" +
                "the border of the world and what can be claimed to factions")
        private final int buffer = 0;

        @Comment("Prevents liquids from flowing outside the world's border.")
        private final boolean preventLiquidFlow = true;

        @Comment("Prevents blocks from having gravity (falling) if outside the world's border.\n" +
                "This includes sand, gravel, anvils, and other physics affected blocks.")
        private final boolean preventBlockGravity = true;

        public int getBuffer() {
            return buffer;
        }

        public boolean isPreventLiquidFlow() {
            return preventLiquidFlow;
        }

        public boolean isPreventBlockGravity() {
            return preventBlockGravity;
        }
    }

    @Comment("The command base (by default f, making the command /f)")
    private final List<String> commandBase = new ArrayList<String>() {
        {
            this.add("f");
        }
    };

    @Comment("FactionsUUID by drtshock\n" +
            "Support and documentation https://factions.support\n" +
            "Updates https://www.spigotmc.org/resources/factionsuuid.1035/\n" +
            "\n" +
            "Made with love <3")
    private final AVeryFriendlyFactionsConfig aVeryFriendlyFactionsConfig = new AVeryFriendlyFactionsConfig();
    @Comment("Tablist settings")
    private final Tablist tablist = new Tablist();
    @Comment("Language file settings")
    private final Lang lang = new Lang();
    @Comment("Colors for relationships and default factions")
    private final Colors colors = new Colors();
    private final Commands commands = new Commands();
    private final Factions factions = new Factions();
    @Comment("What should be logged?")
    private final Logging logging = new Logging();
    @Comment("Controls certain exploit preventions")
    private final Exploits exploits = new Exploits();
    @Comment("Economy support requires Vault and a compatible economy plugin\n" +
            "If you wish to use economy features, be sure to set 'enabled' in this section to true!")
    private final Economy economy = new Economy();
    @Comment("Control for the default settings of /f map")
    private final MapSettings map = new MapSettings();
    @Comment("Data storage settings")
    private final Data data = new Data();
    private final RestrictWorlds restrictWorlds = new RestrictWorlds();
    private final Scoreboard scoreboard = new Scoreboard();
    @Comment("LWC integration\n" +
            "This support targets the modern fork of LWC, called LWC Extended.\n" +
            "You can find it here: https://www.spigotmc.org/resources/lwc-extended.69551/\n" +
            "Note: Modern LWC is no longer supported, and its former maintainer now runs LWC Extended")
    private final LWC lwc = new LWC();
    @Comment("PlayerVaults faction vault settings.\n" +
            "Enable faction-owned vaults!\n" +
            "https://www.spigotmc.org/resources/playervaultsx.51204/")
    private final PlayerVaults playerVaults = new PlayerVaults();
    @Comment("WorldGuard settings")
    private final WorldGuard worldGuard = new WorldGuard();
    private final WorldBorder worldBorder = new WorldBorder();

    public List<String> getCommandBase() {
        return commandBase == null ? FactionsPlugin.DEFAULT_COMMAND_BASE : this.commandBase;
    }

    public AVeryFriendlyFactionsConfig getaVeryFriendlyFactionsConfig() {
        return aVeryFriendlyFactionsConfig;
    }

    public Tablist tablist() {
        return tablist;
    }

    public Lang lang() {
        return lang;
    }

    public Colors colors() {
        return colors;
    }

    public Commands commands() {
        return commands;
    }

    public Factions factions() {
        return factions;
    }

    public Logging logging() {
        return logging;
    }

    public Exploits exploits() {
        return exploits;
    }

    public Economy economy() {
        return economy;
    }

    public MapSettings map() {
        return map;
    }

    public RestrictWorlds restrictWorlds() {
        return restrictWorlds;
    }

    public Scoreboard scoreboard() {
        return scoreboard;
    }

    public PlayerVaults playerVaults() {
        return playerVaults;
    }

    public WorldGuard worldGuard() {
        return worldGuard;
    }

    public LWC lwc() {
        return lwc;
    }

    public WorldBorder worldBorder() {
        return worldBorder;
    }

    public Data data() {
        return data;
    }
}