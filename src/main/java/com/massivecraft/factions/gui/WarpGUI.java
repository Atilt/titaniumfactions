package com.massivecraft.factions.gui;

import com.google.common.collect.ImmutableList;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.math.FastMath;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.util.material.FactionMaterial;
import io.papermc.lib.PaperLib;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.DyeColor;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WarpGUI extends GUI<Integer> {
    private static final SimpleItem WARM_ITEM;
    private static final SimpleItem PASSWORD_MODIFIER;

    static {
        WARM_ITEM = SimpleItem.builder()
                .setName("&8[&7{warp}&8]")
                .setMaterial(FactionMaterial.from("LIME_STAINED_GLASS").get())
                .setColor(DyeColor.LIME)
                .build();
        PASSWORD_MODIFIER = SimpleItem.builder()
                .setMaterial(FactionMaterial.from("BLACK_STAINED_GLASS").get())
                .setColor(DyeColor.BLACK)
                .setLore(ImmutableList.of("&8Password Protected"))
                .build();
    }

    private List<String> warps;
    private final String name;
    private final int page;
    private final Faction faction;

    public WarpGUI(FPlayer user, Faction faction) {
        this(user, -1, faction);
    }

    private WarpGUI(FPlayer user, int page, Faction faction) {
        super(user, getRows(faction));
        this.faction = faction;
        warps = new ObjectArrayList<>(faction.getWarps().keySet());
        if (page == -1 && warps.size() > (5 * 9)) {
            page = 0;
        }
        this.page = page;
        name = page == -1 ? TL.GUI_WARPS_ONE_PAGE.format(faction.getTag()) : TL.GUI_WARPS_PAGE.format(faction.getTag(), Integer.toString(page + 1));
        build();
    }

    @Override
    public String getName() {
        return name;
    }

    private static int getRows(Faction faction) {
        int warpCount = faction.getWarps().size();
        if (warpCount == 0) {
            return 1;
        }
        if (warpCount > 45) {
            return 6;
        }
        return FastMath.ceil(warpCount / 9.0D);
    }

    @Override
    protected String parse(String toParse, Integer index) {
        if (index < 0) {
            return toParse;
        }
        return warps.size() > index ? TextUtil.replace(toParse, "{warp}", warps.get(index)) : toParse;
    }

    @Override
    protected void onClick(Integer index, ClickType clickType) {
        if (!faction.hasAccess(this.user, PermissibleAction.WARP)) {
            user.msg(TL.COMMAND_FWARP_NOACCESS, faction.getTag(user));
            this.user.getPlayer().closeInventory();
            return;
        }
        if (index == -1) {
            int targetPage = page + 1;
            new WarpGUI(this.user, targetPage, faction).open();
            return;
        }
        if (index == -2) {
            int targetPage = page - 1;
            new WarpGUI(this.user, targetPage, faction).open();
            return;
        }
        // Check if there are enough faction warps for this index
        if (warps.size() > index) {
            String warp = warps.get(index);
            if (!faction.hasWarpPassword(warp)) {
                if (transact()) {
                    doWarmup(warp);
                }
            } else {
                Object2ObjectMap<Object, Object> sessionData = new Object2ObjectOpenHashMap<>(1);
                sessionData.put("warp", warp);
                PasswordPrompt passwordPrompt = new PasswordPrompt();
                ConversationFactory inputFactory = new ConversationFactory(FactionsPlugin.getInstance())
                        .withModality(false)
                        .withLocalEcho(false)
                        .withInitialSessionData(sessionData)
                        .withFirstPrompt(passwordPrompt)
                        .addConversationAbandonedListener(passwordPrompt)
                        .withTimeout(5);// TODO get config.getInt("password-timeout", 5)

                user.getPlayer().closeInventory();
                inputFactory.buildConversation(user.getPlayer()).begin();
            }
        }
    }

    @Override
    protected Int2ObjectMap<Integer> createSlotMap() {
        Int2IntMap map = new Int2IntOpenHashMap();
        final int num;
        final int startingIndex;
        if (page == -1) {
            num = warps.size();
            startingIndex = 0;
        } else {
            num = Math.min(warps.size() - ((5 * 9) * page), 5 * 9);
            startingIndex = (5 * 9) * page;
            if (page > 0) {
                map.put(45, -2);
            }
            if (page < (getMaxPages() - 1)) {
                map.put(53, -1);
            }
        }
        int modifier = 0;
        for (int warpIndex = startingIndex, count = 0; count < num; warpIndex++, count++) {
            if (count % 9 == 0) {
                int remaining = num - count;
                if (remaining < 9) {
                    modifier = (9 - remaining) / 2;
                }
            }
            map.put(count + modifier, warpIndex);
        }
        return new Int2ObjectOpenHashMap<>(map); //disgusting
    }

    @Override
    protected SimpleItem getItem(Integer index) {
        if (index == -1) {
            return SimpleItem.builder().setName(TL.GUI_BUTTON_NEXT.toString()).setMaterial(FactionMaterial.from("ARROW").get()).build();
        }
        if (index == -2) {
            return SimpleItem.builder().setName(TL.GUI_BUTTON_PREV.toString()).setMaterial(FactionMaterial.from("ARROW").get()).build();
        }
        SimpleItem item = WARM_ITEM.clone();
        if (faction.hasWarpPassword(warps.get(index))) {
            item.merge(PASSWORD_MODIFIER);
        }
        return item;
    }

    private int getMaxPages() {
        if (warps.size() <= 45) {
            return 0;
        }
        return FastMath.ceil(warps.size() / 45.0D);
    }

    @Override
    protected Map<Integer, SimpleItem> createDummyItems() {
        return Collections.emptyMap();
    }

    private class PasswordPrompt extends StringPrompt implements ConversationAbandonedListener {

        @Override
        public String getPromptText(ConversationContext context) {
            return TL.COMMAND_FWARP_PASSWORD_REQUIRED.toString();
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            String warp = (String) context.getSessionData("warp");
            if (faction.isWarpPassword(warp, input)) {
                // Valid Password, make em pay
                if (transact()) {
                    doWarmup(warp);
                }
            } else {
                // Invalid Password
                user.msg(TL.COMMAND_FWARP_INVALID_PASSWORD);
            }
            return END_OF_CONVERSATION;
        }

        @Override
        public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
            if (abandonedEvent.getCanceller() instanceof ManuallyAbandonedConversationCanceller ||
                    abandonedEvent.getCanceller() instanceof InactivityConversationCanceller) {
                user.msg(TL.COMMAND_FWARP_PASSWORD_CANCEL);
                open();
            }
        }
    }

    private void doWarmup(final String warp) {
        WarmUpUtil.process(user, WarmUpUtil.Warmup.WARP, TL.WARMUPS_NOTIFY_TELEPORT, warp, () -> {
            Player player = user.getPlayer();
            if (player != null) {
                if (!faction.hasAccess(this.user, PermissibleAction.WARP)) {
                    user.msg(TL.COMMAND_FWARP_NOACCESS, faction.getTag(user));
                    return;
                }
                PaperLib.teleportAsync(player, faction.getWarp(warp).getLocation());
                user.msg(TL.COMMAND_FWARP_WARPED, warp);
            }
        }, FactionsPlugin.getInstance().conf().commands().warp().getDelay());
    }

    private boolean transact() {
        if (!user.isAdminBypassing()) {
            return true;
        }

        double cost = FactionsPlugin.getInstance().conf().economy().getCostWarp();

        if (!Econ.shouldBeUsed() || cost == 0.0D || user.isAdminBypassing()) {
            return true;
        }

        if (FactionsPlugin.getInstance().conf().economy().isBankEnabled() && FactionsPlugin.getInstance().conf().economy().isBankFactionPaysCosts() && user.hasFaction() && user.getFaction().hasAccess(user, PermissibleAction.ECONOMY)) {
            return Econ.modifyMoney(user.getFaction(), -cost, TL.COMMAND_FWARP_TOWARP.toString(), TL.COMMAND_FWARP_FORWARPING.toString());
        } else {
            return Econ.modifyMoney(user, -cost, TL.COMMAND_FWARP_TOWARP.toString(), TL.COMMAND_FWARP_FORWARPING.toString());
        }
    }
}
