package com.massivecraft.factions.cooldown;

import com.massivecraft.factions.util.TL;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Duration;
import java.time.Instant;

public final class WildCooldown implements Cooldown {

    private final Instant start;
    private final Duration duration;

    public WildCooldown(Instant start, Duration duration) {
        this.start = start;
        this.duration = duration;
    }

    @Override
    public Instant getStart() {
        return this.start;
    }

    @Override
    public Duration getDuration() {
        return this.duration;
    }

    @Override
    public String getDurationReadable() {
        return DurationFormatUtils.formatDuration(this.duration.toMillis(), TL.COMMAND_WILD_TIMEFORMAT.toString(), true);
    }

    @Override
    public Duration getRemaining() {
        Duration remaining = this.duration.minus(Duration.between(this.start, Instant.now()));
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    @Override
    public String getRemainingReadable() {
        return DurationFormatUtils.formatDuration(this.getRemaining().toMillis(), TL.COMMAND_WILD_TIMEFORMAT.toString(), true);
    }

    @Override
    public boolean isFinished() {
        return getRemaining() == Duration.ZERO;
    }
}
