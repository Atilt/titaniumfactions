package com.massivecraft.factions.cooldown;

import java.time.Duration;
import java.time.Instant;

public interface Cooldown {

    Instant getStart();

    Duration getDuration();

    String getDurationReadable();

    Duration getRemaining();

    String getRemainingReadable();
}
