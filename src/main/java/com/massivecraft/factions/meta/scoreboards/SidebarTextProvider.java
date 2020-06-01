package com.massivecraft.factions.meta.scoreboards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.tag.Tag;

import java.util.List;

public interface SidebarTextProvider {

    String getTitle(FPlayer fPlayer);

    List<String> getText(FPlayer fPlayer);

    static String replaceTags(Faction faction, FPlayer fPlayer, String s) {
        return Tag.parsePlain(faction, fPlayer, Tag.parsePlaceholders(fPlayer.getPlayer(), s));
    }

    static String replaceTags(FPlayer fPlayer, String s) {
        return Tag.parsePlain(fPlayer, Tag.parsePlaceholders(fPlayer.getPlayer(), s));
    }
}