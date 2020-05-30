package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CmdTNTFill extends FCommand {
    public CmdTNTFill() {
        super();

        this.aliases.add("fill");
        this.aliases.add("f");

        this.requiredArgs.add("radius");
        this.requiredArgs.add("amount");

        this.requirements = new CommandRequirements.Builder(Permission.TNT_FILL).withAction(PermissibleAction.TNTWITHDRAW).memberOnly().build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!context.faction.equals(Board.getInstance().getFactionAt(FLocation.wrap(context.player.getLocation())))) {
            context.msg(TL.COMMAND_TNT_TERRITORYONLY);
            return;
        }
        final int radius = context.argAsInt(0, -1);
        final int amount = context.argAsInt(1, -1);

        if (radius <= 0 || amount <= 0) {
            context.msg(TL.COMMAND_TNT_FILL_FAIL_POSITIVE);
            return;
        }

        if (amount > context.faction.getTNTBank()) {
            context.msg(TL.COMMAND_TNT_FILL_FAIL_NOTENOUGH, Integer.toString(amount));
            return;
        }

        if (radius > FactionsPlugin.getInstance().conf().commands().tnt().getMaxRadius()) {
            context.msg(TL.COMMAND_TNT_FILL_FAIL_MAXRADIUS, Integer.toString(radius), Integer.toString(FactionsPlugin.getInstance().conf().commands().tnt().getMaxRadius()));
            return;
        }

        List<DistancedDispenser> dispensers = getDispensers(context.player.getLocation(), radius, context.faction.getIdRaw());

        int remaining = amount;
        int dispenserCount = 0;
        boolean firstRound = true;

        while (remaining > 0 && !dispensers.isEmpty()) {
            int per = Math.max(1, remaining / dispensers.size());
            Iterator<DistancedDispenser> iterator = dispensers.iterator();
            while (iterator.hasNext() && remaining >= per) {
                int left = getCount(iterator.next().getInventory().addItem(getStacks(per)).values());
                remaining -= per - left;
                if (firstRound && ((per - left) > 0)) {
                    dispenserCount++;
                }
                if (left > 0) {
                    iterator.remove();
                }
            }
            firstRound = false;
        }

        context.faction.setTNTBank(context.faction.getTNTBank() - amount + remaining);

        context.msg(TL.COMMAND_TNT_FILL_MESSAGE, Integer.toString(amount - remaining), Integer.toString(dispenserCount), Integer.toString(context.faction.getTNTBank()));
    }

    static ItemStack[] getStacks(int count) {
        if (count < 65) {
            return new ItemStack[]{new ItemStack(Material.TNT, count)};
        } else {
            ObjectList<ItemStack> stack = new ObjectArrayList<>();
            while (count > 0) {
                stack.add(new ItemStack(Material.TNT, Math.min(64, count)));
                count -= Math.min(64, count);
            }
            return stack.toArray(new ItemStack[0]);
        }
    }

    public static class DistancedDispenser implements Comparable<DistancedDispenser> {
        private final double distance;
        private final Inventory inventory;

        public DistancedDispenser(double distance, Inventory inventory) {
            this.distance = distance;
            this.inventory = inventory;
        }

        public double getDistance() {
            return distance;
        }

        public Inventory getInventory() {
            return inventory;
        }

        @Override
        public int compareTo(DistancedDispenser o) {
            return Double.compare(this.distance, o.getDistance());
        }
    }

    static List<DistancedDispenser> getDispensers(Location location, int radius, int id) {
        ObjectList<DistancedDispenser> dispensers = new ObjectArrayList<>();
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = location.getBlock().getRelative(x, y, z);
                    if (block.getType() != Material.DISPENSER || Board.getInstance().getIdRawAt(FLocation.wrap(block)) != id) {
                        continue;
                    }
                    dispensers.add(new DistancedDispenser(location.distanceSquared(block.getLocation()), ((Dispenser) block.getState()).getInventory()));
                }
            }
        }
        Collections.sort(dispensers);
        return dispensers;
    }

    static int getCount(Collection<? extends ItemStack> items) {
        int count = 0;
        for (ItemStack stack : items) {
            count += stack.getAmount();
        }
        return count;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TNT_FILL_DESCRIPTION;
    }
}
