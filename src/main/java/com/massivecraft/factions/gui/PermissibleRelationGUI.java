package com.massivecraft.factions.gui;

import com.google.common.collect.ImmutableMap;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.material.FactionMaterial;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class PermissibleRelationGUI extends GUI<Permissible> {
    private static final Map<Permissible, SimpleItem> ITEMS = new LinkedHashMap<>(8);
    public static final SimpleItem OFFLINE_SWITCH = SimpleItem.builder().setName(TL.GUI_PERMS_TOGGLE.toString()).setMaterial(FactionMaterial.from("LEVER").get()).build();

    static {
        SimpleItem.Builder starter = SimpleItem.builder().setName("&8[{relation-color}{relation}&8]");

        SimpleItem recruit = starter.build();
        recruit.setName(Role.RECRUIT.getTranslation().toString());
        recruit.setMaterial(FactionMaterial.from("WOODEN_SWORD").get());
        ITEMS.put(Role.RECRUIT, recruit);

        SimpleItem normal = starter.build();
        normal.setName(Role.NORMAL.getTranslation().toString());
        normal.setMaterial(FactionMaterial.from("STONE_SWORD").get());
        ITEMS.put(Role.NORMAL, normal);

        SimpleItem moderator = starter.build();
        moderator.setName(Role.MODERATOR.getTranslation().toString());
        moderator.setMaterial(FactionMaterial.from("IRON_SWORD").get());
        ITEMS.put(Role.MODERATOR, moderator);

        SimpleItem coleader = starter.build();
        coleader.setName(Role.COLEADER.getTranslation().toString());
        coleader.setMaterial(FactionMaterial.from("DIAMOND_SWORD").get());
        ITEMS.put(Role.COLEADER, coleader);

        SimpleItem ally = starter.build();
        ally.setName(Relation.ALLY.getTranslation());
        ally.setMaterial(FactionMaterial.from("GOLD_SWORD").get());
        ITEMS.put(Relation.ALLY, ally);

        SimpleItem truce = starter.build();
        truce.setName(Relation.TRUCE.getTranslation());
        truce.setMaterial(FactionMaterial.from("IRON_AXE").get());
        ITEMS.put(Relation.TRUCE, truce);

        SimpleItem neutral = starter.build();
        neutral.setName(Relation.NEUTRAL.getTranslation());
        neutral.setMaterial(FactionMaterial.from("STONE_HOE").get());
        ITEMS.put(Relation.NEUTRAL, neutral);

        SimpleItem enemy = starter.build();
        enemy.setName(Relation.ENEMY.getTranslation());
        enemy.setMaterial(FactionMaterial.from("STONE_AXE").get());
        ITEMS.put(Relation.ENEMY, enemy);
    }

    private final boolean online;

    public PermissibleRelationGUI(boolean online, FPlayer user) {
        super(user, FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ? 2 : 1);
        this.online = online;
        build();
    }

    @Override
    protected String getName() {
        String bit = FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ?
                TL.GUI_PERMS_RELATION_ONLINEOFFLINEBIT.format(this.online ? TL.GUI_PERMS_ONLINE.toString() : TL.GUI_PERMS_OFFLINE.toString())
                : "";
        return TL.GUI_PERMS_RELATION_NAME.format(bit);
    }

    @Override
    protected String parse(String toParse, Permissible permissible) {
        // Uppercase the first letter
        return TextUtil.replace(TextUtil.replace(toParse, "{relation-color}", permissible.getColor().toString()), "{relation}", TextUtil.upperCaseFirst(permissible.toString()));
    }

    @Override
    public void click(int slot, ClickType clickType) {
        if (FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() && slot == 13) {
            new PermissibleRelationGUI(!online, user).open();
        } else {
            super.click(slot, clickType);
        }
    }

    @Override
    protected void onClick(Permissible permissible, ClickType clickType) {
        new PermissibleActionGUI(online, user, permissible).open();
    }

    @Override
    protected Int2ObjectMap<Permissible> createSlotMap() {
        Int2ObjectMap<Permissible> map = new Int2ObjectOpenHashMap<>(8);
        if (online) {
            map.put(0, Role.RECRUIT);
            map.put(1, Role.NORMAL);
            map.put(2, Role.MODERATOR);
            map.put(3, Role.COLEADER);
        }
        map.put(5, Relation.ALLY);
        map.put(6, Relation.NEUTRAL);
        map.put(7, Relation.TRUCE);
        map.put(8, Relation.ENEMY);
        return map;
    }

    @Override
    protected SimpleItem getItem(Permissible permissible) {
        return ITEMS.get(permissible);
    }

    @Override
    protected Map<Integer, SimpleItem> createDummyItems() {
        return FactionsPlugin.getInstance().conf().factions().other().isSeparateOfflinePerms() ? ImmutableMap.of(13, OFFLINE_SWITCH) : Collections.emptyMap();
    }
}
