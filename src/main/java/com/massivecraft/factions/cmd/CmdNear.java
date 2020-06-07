package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public class CmdNear extends FCommand {

    public CmdNear() {
        super();

        this.aliases.add("near");

        this.requirements = new CommandRequirements.Builder(Permission.NEAR)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        int radius = FactionsPlugin.getInstance().conf().commands().near().getRadius();
        ObjectList<FPlayer> nearbyMembers = new ObjectArrayList<>();
        String world = context.fPlayer.getLastStoodAt().getWorldName();
        for (FPlayer fPlayer : context.fPlayer.getFaction().getFPlayersWhereOnline(true)) {
            if (fPlayer == context.fPlayer) {
                continue;
            }
            if (fPlayer.getLastStoodAt().getDistanceSquaredTo(context.fPlayer.getLastStoodAt()) <= radius * radius && fPlayer.getLastStoodAt().getWorldName().equals(world)) {
                nearbyMembers.add(fPlayer);
            }
        }

        StringBuilder playerMessageBuilder = new StringBuilder();
        String playerMessage = TL.COMMAND_NEAR_PLAYER.toString();
        for (FPlayer member : nearbyMembers) {
            playerMessageBuilder.append(parsePlaceholders(context.fPlayer, member, playerMessage));
        }
        // Append none text if no players where found
        if (playerMessageBuilder.toString().isEmpty()) {
            playerMessageBuilder.append(TL.COMMAND_NEAR_NONE);
        }

        context.msg(TL.COMMAND_NEAR_PLAYERLIST.toString().replace("{players-nearby}", playerMessageBuilder.toString()));
    }

    private String parsePlaceholders(FPlayer user, FPlayer target, String string) {
        string = Tag.parsePlain(target, string);
        string = Tag.parsePlaceholders(target.getPlayer(), string);
        string = TextUtil.replace(string, "{role}", target.getRole().toString());
        string = TextUtil.replace(string, "{role-prefix}", target.getRole().getPrefix());
        // Only run distance calculation if needed
        if (string.contains("{distance}")) {
            double distance = FastMath.round(user.getPlayer().getLocation().distance(target.getPlayer().getLocation()));
            string = TextUtil.replace(string,"{distance}", Double.toString(distance));
        }
        return string;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_NEAR_DESCRIPTION;
    }

}