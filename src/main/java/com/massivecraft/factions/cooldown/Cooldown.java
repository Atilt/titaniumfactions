package com.massivecraft.factions.cooldown;

import java.time.Duration;
import java.time.Instant;

public interface Cooldown {

    Cooldown EMPTY = new Cooldown() {
        @Override
        public Instant getStart() {
            return null;
        }

        @Override
        public Duration getDuration() {
            return null;
        }

        @Override
        public String getDurationReadable() {
            return null;
        }

        @Override
        public Duration getRemaining() {
            return null;
        }

        @Override
        public String getRemainingReadable() {
            return null;
        }

        @Override
        public boolean isFinished() {
            return true;
        }
    };


    Instant getStart();

    Duration getDuration();

    String getDurationReadable();

    Duration getRemaining();

    String getRemainingReadable();

    boolean isFinished();
}
