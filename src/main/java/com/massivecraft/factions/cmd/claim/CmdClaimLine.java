package com.massivecraft.factions.cmd.claim;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class CmdClaimLine extends FCommand {

    public CmdClaimLine() {
        super();

        this.aliases.add("claimline");
        this.aliases.add("cl");

        this.optionalArgs.put("amount", "1");
        this.optionalArgs.put("direction", "facing");
        this.optionalArgs.put("faction", "you");

        this.requirements = new CommandRequirements.Builder(Permission.CLAIM_LINE)
                .playerOnly()
                .build();
    }

    public enum ValidCardinal {
        SOUTH(BlockFace.SOUTH),
        WEST(BlockFace.WEST),
        NORTH(BlockFace.NORTH),
        EAST(BlockFace.EAST);

        private BlockFace face;

        public static final ValidCardinal[] VALUES = values();

        ValidCardinal(BlockFace face) {
            this.face = face;
        }

        public BlockFace getFace() {
            return face;
        }
    }

    @Override
    public void perform(CommandContext context) {
        // Args
        Integer amount = context.argAsInt(0, 1); // Default to 1

        if (amount > FactionsPlugin.getInstance().conf().factions().claims().getLineClaimLimit()) {
            context.msg(TL.COMMAND_CLAIMLINE_ABOVEMAX, FactionsPlugin.getInstance().conf().factions().claims().getLineClaimLimit());
            return;
        }

        String direction = context.argAsString(1);
        BlockFace blockFace;

        if (direction == null) {
            blockFace = ValidCardinal.VALUES[Math.round(context.player.getLocation().getYaw() / 90f) & 0x3].getFace();
        } else {
            try {
                blockFace = ValidCardinal.valueOf(direction.toUpperCase()).getFace();
            } catch (IllegalArgumentException exception) {
                context.fPlayer.msg(TL.COMMAND_CLAIMLINE_NOTVALID, direction);
                return;
            }
        }
        Faction forFaction = context.argAsFaction(2, context.faction);
        Location location = context.player.getLocation();

        int bX = blockFace.getModX() << 4;
        int bZ = blockFace.getModZ() << 4;

        // TODO: make this a task like claiming a radius?
        for (int i = 0; i < amount; i++) {
            context.fPlayer.attemptClaim(forFaction, location, true);
            location = location.add(bX, 0, bZ);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CLAIMLINE_DESCRIPTION;
    }
}
