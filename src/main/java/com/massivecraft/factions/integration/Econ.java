package com.massivecraft.factions.integration;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.iface.EconomyParticipator;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.RelationUtil;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class Econ {

    private static Economy econ = null;
    private static OfflinePlayer universeAccount;

    public static void setup() {
        if (isSetup()) {
            return;
        }

        String integrationFail = "Economy integration is " + (FactionsPlugin.getInstance().conf().economy().isEnabled() ? "enabled, but" : "disabled, and") + " the plugin \"Vault\" ";

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            FactionsPlugin.getInstance().getLogger().info(integrationFail + "is not installed.");
            return;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            FactionsPlugin.getInstance().getLogger().info(integrationFail + "is not hooked into an economy plugin.");
            return;
        }
        econ = rsp.getProvider();

        FactionsPlugin.getInstance().getLogger().info("Found economy plugin through Vault: " + econ.getName());

        String account = FactionsPlugin.getInstance().conf().economy().getUniverseAccount();
        if (account != null && !account.isEmpty()) {
            OfflinePlayer possible = Bukkit.getOfflinePlayer(account);
            if (econ.hasAccount(possible)) {
                universeAccount = possible;
            }
        }
        if (!FactionsPlugin.getInstance().conf().economy().isEnabled()) {
            FactionsPlugin.getInstance().getLogger().info("NOTE: Economy is disabled. You can enable it in config/main.conf");
        }

        //P.getInstance().cmdBase.cmdHelp.updateHelp();
    }

    public static boolean shouldBeUsed() {
        return FactionsPlugin.getInstance().conf().economy().isEnabled() && econ != null && econ.isEnabled();
    }

    public static boolean isSetup() {
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static void modifyUniverseMoney(double delta) {
        if (!shouldBeUsed() || universeAccount == null) {
            return;
        }
        modifyBalance(universeAccount, delta);
    }

    public static void sendBalanceInfo(FPlayer to, EconomyParticipator about) {
        if (!shouldBeUsed()) {
            FactionsPlugin.getInstance().log(Level.WARNING, "Vault does not appear to be hooked into an economy plugin.");
            return;
        }
        to.msg(TL.ECON_BALANCE, about.describeTo(to, true), Econ.moneyString(econ.getBalance(Bukkit.getOfflinePlayer(about.getAccountId()))));
    }

    public static void sendBalanceInfo(CommandSender to, Faction about) {
        if (!shouldBeUsed()) {
            FactionsPlugin.getInstance().log(Level.WARNING, "Vault does not appear to be hooked into an economy plugin.");
            return;
        }
        to.sendMessage(ChatColor.stripColor(String.format(TL.ECON_BALANCE.toString(), about.getTag(), Econ.moneyString(econ.getBalance(Bukkit.getOfflinePlayer(about.getAccountId()))))));
    }

    public static boolean canIControlYou(EconomyParticipator i, EconomyParticipator you) {
        Faction fI = RelationUtil.getFaction(i);
        Faction fYou = RelationUtil.getFaction(you);

        // This is a system invoker. Accept it.
        if (fI == null) {
            return true;
        }

        // Bypassing players can do any kind of transaction
        if (i instanceof FPlayer && ((FPlayer) i).isAdminBypassing()) {
            return true;
        }

        // Players with the any withdraw can do.
        if (i instanceof FPlayer && Permission.MONEY_WITHDRAW_ANY.has(((FPlayer) i).getPlayer())) {
            return true;
        }

        // You can deposit to anywhere you feel like. It's your loss if you can't withdraw it again.
        if (i == you) {
            return true;
        }

        // A faction can always transfer away the money of it's members and its own money...
        // This will however probably never happen as a faction does not have free will.
        // Ohh by the way... Yes it could. For daily rent to the faction.
        if (i == fI && fI == fYou) {
            return true;
        }

        // Factions can be controlled by members that are moderators... or any member if any member can withdraw.
        if (i instanceof FPlayer && you instanceof Faction && fI == fYou && (FactionsPlugin.getInstance().conf().economy().isBankMembersCanWithdraw() || ((FPlayer) i).getRole().value >= Role.MODERATOR.value)) {
            return true;
        }

        // Otherwise you may not! ;,,;
        i.msg(TL.ECON_NOPERM, i.describeTo(i, true), you.describeTo(i));
        return false;
    }

    public static boolean transferMoney(EconomyParticipator invoker, EconomyParticipator from, EconomyParticipator to, double amount) {
        return transferMoney(invoker, from, to, amount, true);
    }

    public static boolean transferMoney(EconomyParticipator invoker, EconomyParticipator from, EconomyParticipator to, double amount, boolean notify) {
        if (!shouldBeUsed()) {
            invoker.msg(TL.ECON_OFF);
            return false;
        }

        // The amount must be positive.
        // If the amount is negative we must flip and multiply amount with -1.
        if (amount < 0) {
            amount *= -1;
            EconomyParticipator temp = from;
            from = to;
            to = temp;
        }

        // Check the rights
        if (!canIControlYou(invoker, from)) {
            return false;
        }

        OfflinePlayer fromAcc = Bukkit.getOfflinePlayer(from.getAccountId());
        if (fromAcc.getName() == null) {
            return false;
        }

        OfflinePlayer toAcc = Bukkit.getOfflinePlayer(to.getAccountId());
        if (toAcc.getName() == null) {
            return false;
        }
        // Is there enough money for the transaction to happen?
        if (!econ.has(fromAcc, amount)) {
            // There was not enough money to pay
            if (invoker != null && notify) {
                invoker.msg(TL.ECON_CANTAFFORD_TRANSFER, from.describeTo(invoker, true), moneyString(amount), to.describeTo(invoker));
            }

            return false;
        }

        // Check if the new balance is over Essential's money cap.
        if (Essentials.isOverBalCap(to, econ.getBalance(toAcc) + amount)) {
            invoker.msg(TL.ECON_OVER_BAL_CAP, amount);
            return false;
        }

        // Transfer money
        EconomyResponse erw = econ.withdrawPlayer(fromAcc, amount);

        if (erw.transactionSuccess()) {
            EconomyResponse erd = econ.depositPlayer(toAcc, amount);
            if (erd.transactionSuccess()) {
                if (notify) {
                    sendTransferInfo(invoker, from, to, amount);
                }
                return true;
            } else {
                // transaction failed, refund account
                econ.depositPlayer(fromAcc, amount);
            }
        }

        // if we get here something with the transaction failed
        if (notify) {
            invoker.msg(TL.ECON_TRANSFER_UNABLE, moneyString(amount), to.describeTo(invoker), from.describeTo(invoker, true));
        }

        return false;
    }

    public static Set<FPlayer> getFplayers(EconomyParticipator ep) {
        ObjectSet<FPlayer> fplayers = new ObjectOpenHashSet<>();

        if (ep != null) {
            if (ep instanceof FPlayer) {
                fplayers.add((FPlayer) ep);
            } else if (ep instanceof Faction) {
                fplayers.addAll(((Faction) ep).getFPlayers());
            }
        }

        return fplayers;
    }

    public static void sendTransferInfo(EconomyParticipator invoker, EconomyParticipator from, EconomyParticipator to, double amount) {
        ObjectSet<FPlayer> recipients = new ObjectOpenHashSet<>();
        recipients.addAll(getFplayers(invoker));
        recipients.addAll(getFplayers(from));
        recipients.addAll(getFplayers(to));

        if (invoker == null) {
            for (FPlayer recipient : recipients) {
                recipient.msg(TL.ECON_TRANSFER_NOINVOKER, moneyString(amount), from.describeTo(recipient), to.describeTo(recipient));
            }
        } else if (invoker == from) {
            for (FPlayer recipient : recipients) {
                recipient.msg(TL.ECON_TRANSFER_GAVE, from.describeTo(recipient, true), moneyString(amount), to.describeTo(recipient));
            }
        } else if (invoker == to) {
            for (FPlayer recipient : recipients) {
                recipient.msg(TL.ECON_TRANSFER_TOOK, to.describeTo(recipient, true), moneyString(amount), from.describeTo(recipient));
            }
        } else {
            for (FPlayer recipient : recipients) {
                recipient.msg(TL.ECON_TRANSFER_TRANSFER, invoker.describeTo(recipient, true), moneyString(amount), from.describeTo(recipient), to.describeTo(recipient));
            }
        }
    }

    public static boolean hasAtLeast(EconomyParticipator ep, double delta, String toDoThis) {
        if (!shouldBeUsed()) {
            return true;
        }

        // going the hard way round as econ.has refuses to work.

        OfflinePlayer offline = Bukkit.getOfflinePlayer(ep.getAccountId());
        double currentBalance = offline.getName() == null ? 0 : econ.getBalance(offline);

        if (currentBalance >= delta) { //affordable
            if (toDoThis != null && !toDoThis.isEmpty()) {
                ep.msg(TL.ECON_CANTAFFORD_AMOUNT, ep.describeTo(ep, true), moneyString(delta), toDoThis);
            }
            return false;
        }
        return true;
    }

    public static boolean modifyMoney(EconomyParticipator ep, double delta, String toDoThis, String forDoingThis) {
        if (!shouldBeUsed()) {
            return false;
        }

        OfflinePlayer acc = Bukkit.getOfflinePlayer(ep.getAccountId());
        if (acc.getName() == null) {
            return false;
        }

        String You = ep.describeTo(ep, true);

        if (delta == 0) {
            // no money actually transferred?
//			ep.msg("<h>%s<i> didn't have to pay anything %s.", You, forDoingThis);  // might be for gains, might be for losses
            return true;
        }

        if (delta > 0) {
            // The player should gain money
            // The account might not have enough space
            EconomyResponse er = econ.depositPlayer(acc, delta);
            if (er.transactionSuccess()) {
                modifyUniverseMoney(-delta);
                if (forDoingThis != null && !forDoingThis.isEmpty()) {
                    ep.msg(TL.ECON_GAIN_SUCCESS, You, moneyString(delta), forDoingThis);
                }
                return true;
            } else {
                // transfer to account failed
                if (forDoingThis != null && !forDoingThis.isEmpty()) {
                    ep.msg(TL.ECON_GAIN_FAILURE, You, moneyString(delta), forDoingThis);
                }
                return false;
            }
        } else {
            // The player should loose money
            // The player might not have enough.

            if (econ.has(acc, -delta) && econ.withdrawPlayer(acc, -delta).transactionSuccess()) {
                // There is enough money to pay
                modifyUniverseMoney(-delta);
                if (forDoingThis != null && !forDoingThis.isEmpty()) {
                    ep.msg(TL.ECON_LOST_SUCCESS, You, moneyString(-delta), forDoingThis);
                }
                return true;
            } else {
                // There was not enough money to pay
                if (toDoThis != null && !toDoThis.isEmpty()) {
                    ep.msg(TL.ECON_LOST_FAILURE, You, moneyString(-delta), toDoThis);
                }
                return false;
            }
        }
    }

    public static String moneyString(double amount) {
        return format.format(amount);
    }

    // calculate the cost for claiming land
    public static double calculateClaimCost(int ownedLand, boolean takingFromAnotherFaction) {
        if (!shouldBeUsed()) {
            return 0d;
        }

        // basic claim cost, plus land inflation cost, minus the potential bonus given for claiming from another faction
        return FactionsPlugin.getInstance().conf().economy().getCostClaimWilderness() + (FactionsPlugin.getInstance().conf().economy().getCostClaimWilderness() * FactionsPlugin.getInstance().conf().economy().getClaimAdditionalMultiplier() * ownedLand) - (takingFromAnotherFaction ? FactionsPlugin.getInstance().conf().economy().getCostClaimFromFactionBonus() : 0);
    }

    // calculate refund amount for unclaiming land
    public static double calculateClaimRefund(int ownedLand) {
        return calculateClaimCost(ownedLand - 1, false) * FactionsPlugin.getInstance().conf().economy().getClaimRefundMultiplier();
    }

    // calculate value of all owned land
    public static double calculateTotalLandValue(int ownedLand) {
        double amount = 0;
        for (int x = 0; x < ownedLand; x++) {
            amount += calculateClaimCost(x, false);
        }
        return amount;
    }

    // calculate refund amount for all owned land
    public static double calculateTotalLandRefund(int ownedLand) {
        return calculateTotalLandValue(ownedLand) * FactionsPlugin.getInstance().conf().economy().getClaimRefundMultiplier();
    }


    // -------------------------------------------- //
    // Standard account management methods
    // -------------------------------------------- //

    public static boolean hasAccount(UUID id) {
        return econ.hasAccount(Bukkit.getOfflinePlayer(id));
    }

    public static double getBalance(UUID account) {
        return econ.getBalance(Bukkit.getOfflinePlayer(account));
    }

    private static final DecimalFormat format = new DecimalFormat(TL.ECON_FORMAT.toString());

    public static String getFriendlyBalance(UUID uuid) {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
        if (offline.getName() == null) {
            return "0";
        }
        return format.format(econ.getBalance(offline));
    }

    public static String getFriendlyBalance(FPlayer player) {
        return getFriendlyBalance(player.getId());
    }

    public static boolean setBalance(UUID id, double amount) {
        OfflinePlayer account = Bukkit.getOfflinePlayer(id);
        double current = econ.getBalance(account);
        if (current > amount) {
            return econ.withdrawPlayer(account, current - amount).transactionSuccess();
        } else {
            return econ.depositPlayer(account, amount - current).transactionSuccess();
        }
    }

    private static boolean modifyBalance(OfflinePlayer account, double amount) {
        if (amount < 0) {
            return econ.withdrawPlayer(account, -amount).transactionSuccess();
        } else {
            return econ.depositPlayer(account, amount).transactionSuccess();
        }
    }

    public static boolean modifyBalance(UUID id, double amount) {
        OfflinePlayer account = Bukkit.getOfflinePlayer(id);
        return modifyBalance(account, amount);
    }

    public static boolean deposit(UUID id, double amount) {
        OfflinePlayer account = Bukkit.getOfflinePlayer(id);
        return econ.depositPlayer(account, amount).transactionSuccess();
    }

    public static boolean withdraw(UUID id, double amount) {
        OfflinePlayer account = Bukkit.getOfflinePlayer(id);
        return econ.withdrawPlayer(account, amount).transactionSuccess();
    }

    public static void createAccount(UUID id) {
        OfflinePlayer account = Bukkit.getOfflinePlayer(id);
        if (!econ.createPlayerAccount(account)) {
            FactionsPlugin.getInstance().getLogger().warning("Failed to create economy account for: " + account.getName());
        }
    }
}
