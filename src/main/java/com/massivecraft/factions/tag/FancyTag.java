package com.massivecraft.factions.tag;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.util.QuadFunction;
import com.massivecraft.factions.util.TextUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import net.kyori.text.TextComponent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.gson.GsonComponentSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public enum FancyTag implements Tag {
    ALLIES_LIST("allies-list", (target, fme, prefix, gm) -> processRelation(prefix, target, fme, Relation.ALLY)),
    ENEMIES_LIST("enemies-list", (target, fme, prefix, gm) -> processRelation(prefix, target, fme, Relation.ENEMY)),
    TRUCES_LIST("truces-list", (target, fme, prefix, gm) -> processRelation(prefix, target, fme, Relation.TRUCE)),
    ONLINE_LIST("online-list", (target, fme, prefix, gm) -> {
        List<TextComponent> fancyMessages = new ArrayList<>();
        TextComponent.Builder currentOnline = TextUtil.parseFancy(prefix);
        boolean firstOnline = true;
        for (FPlayer p : MiscUtil.rankOrder(target.getFPlayersWhereOnline(true, fme))) {
            if (fme.getPlayer() != null && !fme.getPlayer().canSee(p.getPlayer())) {
                continue; // skip
            }
            String name = p.getNameAndTitle();
            currentOnline.append(TextComponent.of(firstOnline ? name : ", " + name))
                         .hoverEvent(HoverEvent.showText(TextComponent.of(tipPlayer(p, gm)))).color(TextUtil.kyoriColor(fme.getColorTo(p)));
            firstOnline = false;
            if (GsonComponentSerializer.INSTANCE.serialize(currentOnline.build()).length() > ARBITRARY_LIMIT) {
                fancyMessages.add(currentOnline.build());
                currentOnline = TextComponent.builder();
            }
        }
        fancyMessages.add(currentOnline.build());
        return firstOnline && Tag.isMinimalShow() ? null : fancyMessages;
    }),
    OFFLINE_LIST("offline-list", (target, fme, prefix, gm) -> {
        List<TextComponent> fancyMessages = new ArrayList<>();
        TextComponent.Builder currentOffline = TextUtil.parseFancy(prefix);
        boolean firstOffline = true;
        for (FPlayer p : MiscUtil.rankOrder(target.getFPlayers())) {
            String name = p.getNameAndTitle();
            // Also make sure to add players that are online BUT can't be seen.
            if (!p.isOnline() || (fme.getPlayer() != null && p.isOnline() && !fme.getPlayer().canSee(p.getPlayer()))) {
                currentOffline.append(TextComponent.of(firstOffline ? name : ", " + name))
                              .hoverEvent(HoverEvent.showText(TextComponent.of(tipPlayer(p, gm)))).color(TextUtil.kyoriColor(fme.getColorTo(p)));
                firstOffline = false;
                if (GsonComponentSerializer.INSTANCE.serialize(currentOffline.build()).length() > ARBITRARY_LIMIT) {
                    fancyMessages.add(currentOffline.build());
                    currentOffline = TextComponent.builder();
                }
            }
        }
        fancyMessages.add(currentOffline.build());
        return firstOffline && Tag.isMinimalShow() ? null : fancyMessages;
    }),
    ;

    private final String tag;
    private final QuadFunction<Faction, FPlayer, String, Map<UUID, String>, List<TextComponent>> function;

    private static List<TextComponent> processRelation(String prefix, Faction faction, FPlayer fPlayer, Relation relation) {
        ObjectList<TextComponent> fancyMessages = new ObjectArrayList<>();
        TextComponent.Builder message = TextUtil.parseFancy(prefix);
        boolean first = true;
        for (Faction otherFaction : Factions.getInstance().getAllFactions()) {
            if (otherFaction == faction) {
                continue;
            }
            String s = otherFaction.getTag(fPlayer);
            if (otherFaction.getRelationTo(faction) == relation) {
                message.append(TextComponent.of(first ? s : ", " + s))
                        .hoverEvent(HoverEvent.showText(TextComponent.of(tipFaction(otherFaction, fPlayer))))
                        .color(TextUtil.kyoriColor(fPlayer.getColorTo(otherFaction)));
                first = false;
                if (GsonComponentSerializer.INSTANCE.serialize(message.build()).length() > ARBITRARY_LIMIT) {
                    fancyMessages.add(message.build());
                    message = TextComponent.builder();
                }
            }
        }
        fancyMessages.add(message.build());
        return first && Tag.isMinimalShow() ? null : fancyMessages;
    }

    public static List<TextComponent> parse(String text, Faction faction, FPlayer player, Map<UUID, String> groupMap) {
        for (FancyTag tag : VALUES) {
            if (tag.foundInString(text)) {
                return tag.getMessage(text, faction, player, groupMap);
            }
        }
        return ObjectLists.emptyList(); // We really shouldn't be here.
    }

    public static FancyTag getMatch(String text) {
        for (FancyTag tag : VALUES) {
            if (tag.foundInString(text)) {
                return tag;
            }
        }
        return null;
    }

    private static String tipFaction(Faction faction, FPlayer player) {
        StringBuilder builder = new StringBuilder();
        List<String> tips = FactionsPlugin.getInstance().conf().commands().toolTips().getFaction();
        for (int i = 0; i < tips.size(); i++) {
            String string = Tag.parsePlain(faction, player, tips.get(i));
            if (string == null) {
                continue;
            }
            builder.append(TextUtil.parseColorBukkit(string));
            if (i != tips.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    private static String tipPlayer(FPlayer fplayer, Map<UUID, String> groupMap) {
        StringBuilder builder = new StringBuilder();
        List<String> tips = FactionsPlugin.getInstance().conf().commands().toolTips().getPlayer();
        for (int i = 0; i < tips.size(); i++) {
            String line = tips.get(i);
            String newLine = line;
            everythingOnYourWayOut:
            if (line.contains("{group}")) {
                if (groupMap != null) {
                    String group = groupMap.get(fplayer.getId());
                    if (!group.trim().isEmpty()) {
                        newLine = newLine.replace("{group}", group);
                        break everythingOnYourWayOut;
                    }
                }
                continue;
            }
            String string = Tag.parsePlain(fplayer, newLine);
            if (string == null) {
                continue;
            }
            builder.append(TextUtil.parseColorBukkit(string));
            if (i != tips.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }

    FancyTag(String tag, QuadFunction<Faction, FPlayer, String, Map<UUID, String>, List<TextComponent>> function) {
        this.tag = '{' + tag + '}';
        this.function = function;
    }

    @Override
    public String getTag() {
        return this.tag;
    }

    @Override
    public boolean foundInString(String test) {
        return test != null && test.contains(this.tag);
    }

    public List<TextComponent> getMessage(String text, Faction faction, FPlayer player, Map<UUID, String> groupMap) {
        if (!this.foundInString(text)) {
            return ObjectLists.emptyList(); // We really, really shouldn't be here.
        }
        return this.function.apply(faction, player, TextUtil.replace(text, this.getTag(), ""), groupMap);
    }
    
    public static final FancyTag[] VALUES = FancyTag.values();
}
