package com.massivecraft.factions.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GUI<Type> implements InventoryHolder {
    protected Inventory inventory;

    protected final int size;
    protected int back = -1;

    private Map<Integer, Type> slotMap = new HashMap<>();

    protected FPlayer user;

    public GUI(FPlayer user, int rows) {
        this.size = rows * 9;
        this.user = user;
    }

    protected abstract String getName();

    // Parse all the placeholder values in this String, will be injected into the SimpleItem and return it
    protected abstract String parse(String toParse, Type type);

    protected abstract void onClick(Type action, ClickType clickType);

    // Should only be called by the InventoryListener
    public void click(int slot, ClickType clickType) {
        if (slotMap.containsKey(slot)) {
            onClick(slotMap.get(slot), clickType);
        } else if (this instanceof Backable && back == slot) {
            ((Backable) this).onBack();
        }
    }

    protected abstract Map<Integer, Type> createSlotMap();

    public void build() {
        String guiName = this.getName();
        inventory = Bukkit.createInventory(this, size, guiName);

        this.slotMap = this.createSlotMap();

        buildDummyItems();
        buildItems();
    }

    protected abstract SimpleItem getItem(Type type);

    protected void buildItems() {
        for (Map.Entry<Integer, Type> entry : slotMap.entrySet()) {
            setItemAtSlot(entry.getKey(), entry.getValue());
        }
    }

    protected void buildItem(Type type) {
        for (Map.Entry<Integer, Type> entry : slotMap.entrySet()) {
            if (entry.getValue().equals(type)) {
                setItemAtSlot(entry.getKey(), entry.getValue());
            }
        }
    }

    private void setItemAtSlot(int slot, Type type) {
        SimpleItem item = getItem(type);
        parse(item, type);
        inventory.setItem(slot, item.get());
    }

    protected abstract Map<Integer, SimpleItem> createDummyItems();

    protected void buildDummyItems() {
        for (Map.Entry<Integer, SimpleItem> entry : createDummyItems().entrySet()) {
            SimpleItem item = entry.getValue();
            parse(item, null);
            inventory.setItem(entry.getKey(), item.get());
        }
    }

    // Will parse default faction stuff, ie: Faction Name, Power, Colors etc
    protected void parse(SimpleItem item, Type type) {
        if (type != null) {
            item.setName(parse(item.getName(), type));
        }
        item.setName(parseDefault(item.getName()));
        if (item.getLore() != null) {
            item.setLore(parseList(item.getLore(), type));
        }
    }

    protected List<String> parseList(List<String> stringList, Type type) {
        List<String> newList = new ArrayList<>();
        for (String toParse : stringList) {
            String parsed = toParse;
            if (type != null) {
                parsed = parse(parsed, type);
            }
            parsed = parseDefault(parsed);
            newList.add(parsed);
        }
        return newList;
    }

    protected String parseDefault(String toParse) {
        toParse = TextUtil.parseColor(toParse);
        toParse = Tag.parsePlain(user, toParse);
        toParse = Tag.parsePlain(user.getFaction(), toParse);
        toParse = Tag.parsePlaceholders(user.getPlayer(), toParse);
        return toParse;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open() {
        user.getPlayer().openInventory(getInventory());
    }

    public interface Backable {
        void onBack();
    }
}
