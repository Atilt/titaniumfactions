package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.gui.PermissibleActionGUI;
import com.massivecraft.factions.gui.PermissibleRelationGUI;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

import java.util.Arrays;

public class CmdPerm extends FCommand {

    public CmdPerm() {
        super();

        this.aliases.add("perm");
        this.aliases.add("perms");
        this.aliases.add("permission");
        this.aliases.add("permissions");

        this.optionalArgs.put("relation", "relation");
        this.optionalArgs.put("action", "action");
        this.optionalArgs.put("access", "access");

        this.requirements = new CommandRequirements.Builder(Permission.PERMISSIONS)
                .memberOnly()
                .withRole(Role.ADMIN)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (context.args.size() == 0) {
            new PermissibleRelationGUI(true, context.fPlayer).open();
            return;
        } else if (context.args.size() == 1 && getPermissible(context.argAsString(0)) != null) {
            new PermissibleActionGUI(true, context.fPlayer, getPermissible(context.argAsString(0))).open();
            return;
        }

        // If not opening GUI, then setting the permission manually.
        if (context.args.size() < 3) {
            context.fPlayer.msg(TL.COMMAND_PERM_DESCRIPTION);
            return;
        }

        ObjectSet<Permissible> permissibles = new ObjectOpenHashSet<>();
        ObjectSet<PermissibleAction> permissibleActions = new ObjectOpenHashSet<>();

        boolean allRelations = context.argAsString(0).equalsIgnoreCase("all");
        boolean allActions = context.argAsString(1).equalsIgnoreCase("all");
        boolean online = true;
        if (FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() && context.args.size() == 4 && "offline".equalsIgnoreCase(context.argAsString(3))) {
            online = false;
        }

        if (allRelations) {
            permissibles.addAll(context.faction.getPermissions().keySet());
        } else {
            Permissible permissible = getPermissible(context.argAsString(0));

            if (permissible == null) {
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_RELATION);
                return;
            }

            permissibles.add(permissible);
        }

        if (allActions) {
            permissibleActions.addAll(Arrays.asList(PermissibleAction.values()));
        } else {
            PermissibleAction permissibleAction = PermissibleAction.fromString(context.argAsString(1));
            if (permissibleAction == null) {
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_ACTION);
                return;
            }

            permissibleActions.add(permissibleAction);
        }

        boolean access;
        switch (context.argAsString(2).toLowerCase()) {
            case "allow":
            case "true":
                access = true;
                break;
            case "deny":
            case "false":
                access = false;
                break;
            default:
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_ACCESS);
                return;
        }

        for (Permissible permissible : permissibles) {
            for (PermissibleAction permissibleAction : permissibleActions) {
                context.fPlayer.getFaction().setPermission(online, permissible, permissibleAction, access);
            }
        }

        context.fPlayer.msg(TL.COMMAND_PERM_SET, context.argAsString(1), access, context.argAsString(0));
        FactionsPlugin.getInstance().log(String.format(TL.COMMAND_PERM_SET.toString(), context.argAsString(1), access, context.argAsString(0)) + " for faction " + context.fPlayer.getTag());
    }

    private Permissible getPermissible(String name) {
        Role role = Role.fromString(name.toUpperCase());
        if (role != null) {
            return role;
        }
        return Relation.fromString(name.toUpperCase());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_PERM_DESCRIPTION;
    }

}
