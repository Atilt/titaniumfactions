package com.massivecraft.factions.logging;

import com.massivecraft.factions.util.TextUtil;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class FactionsLogger extends Logger {

    private final String prefix;

    public FactionsLogger(Plugin context, String prefix) {
        super(context.getClass().getCanonicalName(), null);
        this.prefix = prefix;

        this.setParent(context.getServer().getLogger());
        this.setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(TextUtil.parseAnsi(this.prefix + logRecord.getMessage()));
        super.log(logRecord);
    }
}