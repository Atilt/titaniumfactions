package com.massivecraft.factions.cmd.money;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;


public class CmdMoneyTransferFp extends MoneyCommand {

    public CmdMoneyTransferFp() {
        super();

        this.aliases.add("fp");

        this.requiredArgs.add("amount");
        this.requiredArgs.add("faction");
        this.requiredArgs.add("player");

        this.requirements = new CommandRequirements.Builder(Permission.MONEY_F2P).build();
    }

    @Override
    public void perform(CommandContext context) {
        double amount = context.argAsDouble(0, 0d);
        EconomyParticipator from = context.argAsFaction(1);
        if (from == null) {
            return;
        }
        EconomyParticipator to = context.argAsBestFPlayerMatch(2);
        if (to == null) {
            return;
        }
        if (Econ.transferMoney(context.fPlayer, from, to, amount) && FactionsPlugin.getInstance().conf().logging().isMoneyTransactions()) {
            FactionsPlugin.getInstance().getPluginLogger().info(TL.COMMAND_MONEYTRANSFERFP_TRANSFER.format(context.fPlayer.getName(), Econ.moneyString(amount), from.describeTo(null), to.describeTo(null)));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_MONEYTRANSFERFP_DESCRIPTION;
    }
}
