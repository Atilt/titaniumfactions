package com.massivecraft.factions.cmd;

import com.drtshock.playervaults.PlayerVaults;
import com.drtshock.playervaults.translations.Lang;
import com.drtshock.playervaults.vaultmanagement.VaultManager;
import com.drtshock.playervaults.vaultmanagement.VaultOperations;
import com.drtshock.playervaults.vaultmanagement.VaultViewInfo;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.FastUUID;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Set;

public class CmdVault extends FCommand {

    private static MessageFormat VAULT_PREFIX = new MessageFormat(FactionsPlugin.getInstance().conf().playerVaults().getVaultPrefix());

    public CmdVault() {
        super();

        this.aliases.add("vault");

        this.optionalArgs.put("number", "number");

        this.requirements = new CommandRequirements.Builder(Permission.VAULT)
                .memberOnly()
                .noDisableOnLock()
                .brigadier(VaultBrigadier.class)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        /*
             /f vault <number>
         */

        Player player = context.player;
        String uuid = FastUUID.toString(player.getUniqueId());

        if (PlayerVaults.getInstance().getInVault().containsKey(uuid)) {
            return; // Already in a vault so they must be trying to dupe.
        }

        int number = context.argAsInt(0, 0); // Default to 0 or show on 0
        int max = context.faction.getMaxVaults();
        if (number > max) {
            player.sendMessage(TL.COMMAND_VAULT_TOOHIGH.format(Integer.toString(number), Integer.toString(max)));
            return;
        }
        //pre-compile
        String vaultName = VAULT_PREFIX.format(Integer.toString(context.faction.getIdRaw()));

        if (number < 1) {
            // Message about which vaults that Faction has.
            // List the target
            YamlConfiguration file = VaultManager.getInstance().getPlayerVaultFile(vaultName, false);
            if (file == null) {
                context.sender.sendMessage(Lang.TITLE.toString() + Lang.VAULT_DOES_NOT_EXIST.toString());
            } else {
                Set<String> keys = file.getKeys(false);
                StringBuilder sb = new StringBuilder(keys.size() * 2);
                for (String key : keys) {
                    sb.append(TextUtil.replace(key, "vault", "")).append(" ");
                }

                context.sender.sendMessage(Lang.TITLE.toString() + Lang.EXISTING_VAULTS.toString().replaceAll("%p", context.fPlayer.getTag()).replaceAll("%v", sb.toString().trim()));
            }
            return;
        } // end listing vaults.

        // Attempt to open vault.
        if (VaultOperations.openOtherVault(player, vaultName, Integer.toString(number))) {
            // Success
            PlayerVaults.getInstance().getInVault().put(uuid, new VaultViewInfo(vaultName, number));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_VAULT_DESCRIPTION;
    }

    protected static class VaultBrigadier implements BrigadierProvider {
        @Override
        public ArgumentBuilder<Object, ?> get(ArgumentBuilder<Object, ?> parent) {
            return parent.then(RequiredArgumentBuilder.argument("number", IntegerArgumentType.integer(0, 99)));
        }
    }

    public static void reload() {
        VAULT_PREFIX = new MessageFormat(FactionsPlugin.getInstance().conf().playerVaults().getVaultPrefix());
    }
}
