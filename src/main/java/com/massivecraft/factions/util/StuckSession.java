package com.massivecraft.factions.util;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.time.Instant;

public final class StuckSession implements AutoCloseable {

    private final Instant start;
    private final Duration duration;

    private int taskId = -1;

    public StuckSession(Instant start, Duration duration) {
        this.start = start;
        this.duration = duration;
    }

    public Instant getStart() {
        return this.start;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public String getDurationReadable() {
        return DurationFormatUtils.formatDuration(this.duration.toMillis(), TL.COMMAND_STUCK_TIMEFORMAT.toString(), true);
    }

    public Duration getRemaining() {
        Duration remaining = this.duration.minus(Duration.between(this.start, Instant.now()));
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public String getRemainingReadable() {
        return DurationFormatUtils.formatDuration(this.getRemaining().toMillis(), TL.COMMAND_STUCK_TIMEFORMAT.toString(), true);
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void close() {
        if (this.taskId != -1) {
            Bukkit.getScheduler().cancelTask(this.taskId);
            this.taskId = -1;
        }
    }
}