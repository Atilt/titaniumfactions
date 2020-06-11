package com.massivecraft.factions.config.file;

import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.ConfigName;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;

public class DefaultPermissionsConfig {
    public static class Permissions {
        public static class PermissiblePermInfo {
            public boolean isLocked() {
                return this.locked;
            }

            public boolean defaultAllowed() {
                return this.value;
            }

            private final boolean locked = false;
            @ConfigName("default")
            private boolean value = false;
        }

        public class FactionOnlyPermInfo {
            protected PermissiblePermInfo coleader = new PermissiblePermInfo();
            protected PermissiblePermInfo moderator = new PermissiblePermInfo();
            protected PermissiblePermInfo normal = new PermissiblePermInfo();
            protected PermissiblePermInfo recruit = new PermissiblePermInfo();

            public PermissiblePermInfo get(Permissible permissible) {
                if (permissible instanceof Role) {
                    switch ((Role) permissible) {
                        case COLEADER:
                            return this.coleader;
                        case MODERATOR:
                            return this.moderator;
                        case NORMAL:
                            return this.normal;
                        case RECRUIT:
                            return this.recruit;
                    }
                }
                // TODO print warning
                return null;
            }
        }

        public class FullPermInfo extends FactionOnlyPermInfo {
            protected final PermissiblePermInfo ally = new PermissiblePermInfo();
            protected final PermissiblePermInfo truce = new PermissiblePermInfo();
            protected final PermissiblePermInfo neutral = new PermissiblePermInfo();
            protected final PermissiblePermInfo enemy = new PermissiblePermInfo();

            public PermissiblePermInfo get(Permissible permissible) {
                if (permissible instanceof Relation) {
                    switch ((Relation) permissible) {
                        case ALLY:
                            return this.ally;
                        case TRUCE:
                            return this.truce;
                        case NEUTRAL:
                            return this.neutral;
                        case ENEMY:
                            return this.enemy;
                    }
                }
                return super.get(permissible);
            }
        }

        public FactionOnlyPermInfo getBan() {
            return this.ban;
        }

        public FullPermInfo getBuild() {
            return this.build;
        }

        public FullPermInfo getDestroy() {
            return this.destroy;
        }

        public FullPermInfo getFrostWalk() {
            return this.frostWalk;
        }

        public FullPermInfo getPainBuild() {
            return this.painBuild;
        }

        public FullPermInfo getDoor() {
            return this.door;
        }

        public FullPermInfo getButton() {
            return this.button;
        }

        public FullPermInfo getLever() {
            return this.lever;
        }

        public FullPermInfo getContainer() {
            return this.container;
        }

        public FactionOnlyPermInfo getInvite() {
            return this.invite;
        }

        public FactionOnlyPermInfo getKick() {
            return this.kick;
        }

        public FullPermInfo getItem() {
            return this.item;
        }

        public FullPermInfo getHome() {
            return this.home;
        }

        public FactionOnlyPermInfo getSetHome() {
            return this.sethome;
        }

        public FactionOnlyPermInfo getEconomy() {
            return this.economy;
        }

        public FactionOnlyPermInfo getTerritory() {
            return this.territory;
        }

        public FactionOnlyPermInfo getTNTDeposit() {
            return this.tntDeposit;
        }

        public FactionOnlyPermInfo getTNTWithdraw() {
            return this.tntWithdraw;
        }

        public FactionOnlyPermInfo getOwner() {
            return this.owner;
        }

        public FullPermInfo getPlate() {
            return this.plate;
        }

        public FactionOnlyPermInfo getDisband() {
            return this.disband;
        }

        public FactionOnlyPermInfo getPromote() {
            return this.promote;
        }

        public FactionOnlyPermInfo getSetWarp() {
            return this.setwarp;
        }

        public FullPermInfo getWarp() {
            return this.warp;
        }

        public FullPermInfo getWild() {
            return this.wild;
        }

        public FullPermInfo getFly() {
            return this.fly;
        }

        @Comment("Can ban others from the faction")
        private final FactionOnlyPermInfo ban = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can build in faction territory (while not raidable)")
        private final FullPermInfo build = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can destroy in faction territory (while not raidable)")
        private final FullPermInfo destroy = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can frost walk in faction territory (while not raidable)")
        private final FullPermInfo frostWalk = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Allows building/destroying in faction territory but causes pain (while not raidable)")
        private final FullPermInfo painBuild = new FullPermInfo();
        @Comment("Use doors in faction territory (while not raidable)")
        private final FullPermInfo door = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use buttons in faction territory (while not raidable)")
        private final FullPermInfo button = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use levers in faction territory (while not raidable)")
        private final FullPermInfo lever = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use containers in faction territory (while not raidable)")
        private final FullPermInfo container = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Able to invite others to the faction")
        private final FactionOnlyPermInfo invite = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        private final FactionOnlyPermInfo kick = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Use items in faction territory (while not raidable)")
        private final FullPermInfo item = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can visit the faction home")
        private final FullPermInfo home = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can set the faction home")
        private final FactionOnlyPermInfo sethome = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
            }
        };
        @Comment("Can access faction economy")
        private final FactionOnlyPermInfo economy = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
            }
        };
        @Comment("Can claim/unclaim faction territory")
        private final FactionOnlyPermInfo territory = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can deposit TNT into the bank")
        private final FactionOnlyPermInfo tntDeposit = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can withdraw TNT from the bank")
        private final FactionOnlyPermInfo tntWithdraw = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can created owned areas with /f owner")
        private final FactionOnlyPermInfo owner = new FactionOnlyPermInfo();
        @Comment("Can interact with plates")
        private final FullPermInfo plate = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        private final FactionOnlyPermInfo disband = new FactionOnlyPermInfo();
        @Comment("Can promote members up to their own role within the faction")
        private final FactionOnlyPermInfo promote = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can set a faction warp")
        private final FactionOnlyPermInfo setwarp = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can use faction warps")
        private final FullPermInfo warp = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can use faction wild teleport")
        private final FullPermInfo wild = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can fly in faction territory")
        private final FullPermInfo fly = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
    }

    @Comment("Permissions settings\n" +
            "Each main section represents one permission.\n" +
            "Inside is each relation.\n" +
            "Each relation has a default value (true=allowed, false=disallowed)\n" +
            "  and true/false for if it's locked to editing by factions admins.")
    private final Permissions permissions = new Permissions();

    public Permissions getPermissions() {
        return this.permissions;
    }
}
