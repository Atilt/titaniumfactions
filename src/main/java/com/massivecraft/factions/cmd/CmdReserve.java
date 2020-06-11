package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.reserves.Reserve;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class CmdReserve extends FCommand {

    public CmdReserve() {
        super();

        this.aliases.add("reserve");
        this.aliases.add("res");

        this.requiredArgs.add("add/remove");
        this.requiredArgs.add("player");
        this.optionalArgs.put("tag", "");

        this.requirements = new CommandRequirements.Builder(Permission.RESERVE)
                .noDisableOnLock()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        String action = context.argAsString(0).toLowerCase();
        if (action.equals("add")) {
            if (!context.argIsSet(2)) {
                context.msg(TL.COMMAND_RESERVE_NO_TAG_SET);
                return;
            }
            if (Factions.getInstance().getByTag(context.argAsString(2)) != null) {
                context.msg(TL.COMMAND_RESERVE_TAG_TAKEN);
                return;
            }
            String name = context.argAsString(1);
            this.executePlayer(name, offlinePlayer -> {
                UUID uuid = offlinePlayer.getUniqueId();
                Reserve reserve = this.plugin.getReserveManager().getReserve(uuid);
                if (reserve != null) {
                    context.msg(TL.COMMAND_RESERVE_ALREADY_HAS_RESERVE.format(reserve.getTag()));
                    return;
                }
                String tag = context.argAsString(2);
                if (Factions.getInstance().getByTag(tag) != null) {
                    context.msg(TL.COMMAND_RESERVE_TAG_TAKEN);
                    return;
                }
                this.plugin.getReserveManager().reserve(uuid, new Reserve(offlinePlayer.getName(), tag));
                context.msg(TL.COMMAND_RESERVE_RESERVED.format(tag, offlinePlayer.getName()));
            });
            return;
        }
        if (action.equals("remove")) {
            String name = context.argAsString(1);
            this.executePlayer(name, offlinePlayer -> {
                UUID uuid = offlinePlayer.getUniqueId();
                Reserve reserve = this.plugin.getReserveManager().unreserve(uuid);
                if (reserve == null) {
                    context.msg(TL.COMMAND_RESERVE_NO_RESERVE);
                    return;
                }
                context.msg(TL.COMMAND_RESERVE_UNRESERVED.format(reserve.getTag(), offlinePlayer.getName()));
            });
        }
    }

    private void executePlayer(String name, Consumer<OfflinePlayer> player) {
        Player attempt = Bukkit.getPlayer(name);
        if (attempt == null) {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
                Bukkit.getScheduler().runTask(this.plugin, () -> player.accept(offlinePlayer));
            });
            return;
        }
        player.accept(attempt);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RESERVE_DESCRIPTION;
    }
}
