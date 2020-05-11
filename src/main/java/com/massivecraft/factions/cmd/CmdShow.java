package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tag.FactionTag;
import com.massivecraft.factions.tag.FancyTag;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CmdShow extends FCommand {

    private final ObjectList<String> defaults = new ObjectArrayList<>();

    public CmdShow() {
        this.aliases.add("show");
        this.aliases.add("who");

        // add defaults to /f show in case config doesnt have it
        defaults.add("{header}");
        defaults.add("<a>Description: <i>{description}");
        defaults.add("<a>Joining: <i>{joining}    {peaceful}");
        defaults.add("<a>Land / Power / Maxpower: <i> {chunks} / {power} / {maxPower}");
        defaults.add("<a>Raidable: <i>{raidable}");
        defaults.add("<a>Founded: <i>{create-date}");
        defaults.add("<a>This faction is permanent, remaining even with no members.");
        defaults.add("<a>Land value: <i>{land-value} {land-refund}");
        defaults.add("<a>Balance: <i>{faction-balance}");
        defaults.add("<a>Bans: <i>{faction-bancount}");
        defaults.add("<a>Allies(<i>{allies}<a>/<i>{max-allies}<a>): {allies-list}");
        defaults.add("<a>Online: (<i>{online}<a>/<i>{members}<a>): {online-list}");
        defaults.add("<a>Offline: (<i>{offline}<a>/<i>{members}<a>): {offline-list}");

        this.optionalArgs.put("faction tag", "yours");

        this.requirements = new CommandRequirements.Builder(Permission.SHOW).noDisableOnLock().build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.faction;
        if (context.argIsSet(0)) {
            faction = context.argAsFaction(0);
        }
        if (faction == null) {
            return;
        }

        if (context.fPlayer != null && !context.player.hasPermission(Permission.SHOW_BYPASS_EXEMPT.toString())
                && FactionsPlugin.getInstance().conf().commands().show().getExempt().contains(faction.getTag())) {
            context.msg(TL.COMMAND_SHOW_EXEMPT);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!context.payForCommand(FactionsPlugin.getInstance().conf().economy().getCostShow(), TL.COMMAND_SHOW_TOSHOW, TL.COMMAND_SHOW_FORSHOW)) {
            return;
        }

        List<String> show = FactionsPlugin.getInstance().conf().commands().show().getFormat();
        if (show == null || show.isEmpty()) {
            show = defaults;
        }

        if (!faction.isNormal()) {
            String tag = faction.getTag(context.fPlayer);
            // send header and that's all
            String header = show.get(0);
            if (FactionTag.HEADER.foundInString(header)) {
                context.msg(TextUtil.titleize(tag));
            } else {
                String message = TextUtil.replace(header, FactionTag.FACTION.getTag(), tag);
                message = Tag.parsePlain(faction, context.fPlayer, message);
                context.msg(TextUtil.parse(message));
            }
            return; // we only show header for non-normal factions
        }

        ObjectList<String> messageList = new ObjectArrayList<>();
        for (String raw : show) {
            String parsed = Tag.parsePlain(faction, context.fPlayer, raw); // use relations
            if (parsed == null) {
                continue; // Due to minimal f show.
            }

            if (context.fPlayer != null) {
                parsed = Tag.parsePlaceholders(context.fPlayer.getPlayer(), parsed);
            }

            if (!parsed.contains("{notFrozen}") && !parsed.contains("{notPermanent}")) {
                if (parsed.contains("{ig}")) {
                    // replaces all variables with no home TL
                    parsed = parsed.substring(0, parsed.indexOf("{ig}")) + TL.COMMAND_SHOW_NOHOME.toString();
                }
                messageList.add(TextUtil.replace(parsed, "%", ""));
            }
        }
        if (context.fPlayer != null && this.groupPresent()) {
            new GroupGetter(messageList, context.fPlayer, faction).runTaskAsynchronously(FactionsPlugin.getInstance());
        } else {
            this.sendMessages(messageList, context.sender, faction, context.fPlayer);
        }
    }

    private void sendMessages(List<String> messageList, CommandSender recipient, Faction faction, FPlayer player) {
        this.sendMessages(messageList, recipient, faction, player, null);
    }

    private void sendMessages(List<String> messageList, CommandSender recipient, Faction faction, FPlayer player, Map<UUID, String> groupMap) {
        FancyTag tag;
        for (String parsed : messageList) {
            if ((tag = FancyTag.getMatch(parsed)) != null) {
                if (player != null) {
                    List<TextComponent> fancy = FancyTag.parse(parsed, faction, player, groupMap);
                    if (fancy != null) {
                        for (TextComponent fancyMessage : fancy) {
                            TextAdapter.sendMessage(recipient, fancyMessage);
                        }
                    }
                } else {
                    StringBuilder builder = new StringBuilder();
                    builder.append(TextUtil.replace(parsed, tag.getTag(), ""));
                    switch (tag) {
                        case ONLINE_LIST:
                            this.onOffLineMessage(builder, recipient, faction, true);
                            break;
                        case OFFLINE_LIST:
                            this.onOffLineMessage(builder, recipient, faction, false);
                            break;
                        case ALLIES_LIST:
                            this.relationMessage(builder, recipient, faction, Relation.ALLY);
                            break;
                        case ENEMIES_LIST:
                            this.relationMessage(builder, recipient, faction, Relation.ENEMY);
                            break;
                        case TRUCES_LIST:
                            this.relationMessage(builder, recipient, faction, Relation.TRUCE);
                            break;
                        default:
                            // NO
                    }
                }
            } else {
                recipient.sendMessage(TextUtil.parse(parsed));
            }
        }
    }

    private void onOffLineMessage(StringBuilder builder, CommandSender recipient, Faction faction, boolean online) {
        boolean first = true;
        for (FPlayer p : MiscUtil.rankOrder(faction.getFPlayersWhereOnline(online))) {
            String name = p.getNameAndTitle();
            builder.append(first ? name : ", " + name);
            first = false;
        }
        recipient.sendMessage(TextUtil.parse(builder.toString()));
    }

    private void relationMessage(StringBuilder builder, CommandSender recipient, Faction faction, Relation relation) {
        boolean first = true;
        for (Faction otherFaction : Factions.getInstance().getAllFactions()) {
            if (otherFaction != faction && otherFaction.getRelationTo(faction) == relation) {
                String s = otherFaction.getTag();
                builder.append(first ? s : ", " + s);
                first = false;
            }
        }
        recipient.sendMessage(TextUtil.parse(builder.toString()));
    }

    private boolean groupPresent() {
        for (String line : FactionsPlugin.getInstance().conf().commands().toolTips().getPlayer()) {
            if (line.contains("{group}")) {
                return true;
            }
        }
        return false;
    }

    private class GroupGetter extends BukkitRunnable {
        private List<String> messageList;
        private FPlayer sender;
        private Faction faction;
        private Set<OfflinePlayer> players;

        private GroupGetter(List<String> messageList, FPlayer sender, Faction faction) {
            this.messageList = messageList;
            this.sender = sender;
            this.faction = faction;

            Set<FPlayer> players = faction.getFPlayers();
            this.players = new ObjectOpenHashSet<>(players.size());
            for (FPlayer player : players) {
                this.players.add(Bukkit.getOfflinePlayer(player.getId()));
            }
        }

        @Override
        public void run() {
            Object2ObjectMap<UUID, String> map = new Object2ObjectOpenHashMap<>();
            for (OfflinePlayer player : this.players) {
                map.put(player.getUniqueId(), FactionsPlugin.getInstance().getPrimaryGroup(player));
            }
            new Sender(this.messageList, this.sender, this.faction, map).runTask(FactionsPlugin.getInstance());
        }
    }

    private class Sender extends BukkitRunnable {
        private List<String> messageList;
        private FPlayer sender;
        private Faction faction;
        private Map<UUID, String> map;

        private Sender(List<String> messageList, FPlayer sender, Faction faction, Map<UUID, String> map) {
            this.messageList = messageList;
            this.sender = sender;
            this.faction = faction;
            this.map = map;
        }

        @Override
        public void run() {
            Player player = Bukkit.getPlayer(sender.getName());
            if (player != null) {
                CmdShow.this.sendMessages(messageList, player, faction, sender, map);
            }
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOW_COMMANDDESCRIPTION;
    }

}