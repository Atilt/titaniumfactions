package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.meta.scoreboards.SidebarProvider;
import com.massivecraft.factions.meta.tablist.TablistProvider;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;

public class CmdReload extends FCommand {

    public CmdReload() {
        super();

        this.aliases.add("reload");

        this.requirements = new CommandRequirements.Builder(Permission.RELOAD)
                .noDisableOnLock()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        long start = System.nanoTime();
        FactionsPlugin.getInstance().getConfigManager().loadConfigs();
        FactionsPlugin.getInstance().getWildManager().deserialize(this.plugin.getPath().resolve("config").resolve("wild.conf"), wildWorlds -> {
            FactionsPlugin.getInstance().getReserveManager().deserialize(this.plugin.getPath().resolve("reserves.json"), reserves -> {
                FactionsPlugin.getInstance().loadLang();
                SidebarProvider.get().trackAll();
                TablistProvider.get().trackAll();
                CmdVault.reload();
                context.msg(TL.COMMAND_RELOAD_TIME, Double.toString((System.nanoTime() - start) / 1_000_000.0D));
            });
        });
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RELOAD_DESCRIPTION;
    }
}