package com.massivecraft.factions.tasks;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.MultiClaim;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class CompactSpiralTask extends BukkitRunnable implements MultiClaim {

    public static final int MAX_DEBUG = 34;

    private final World world;

    private final FPlayer fPlayer;
    private final Faction faction;
    
    private final int limit;
    private int current;
    private int length = -1;
    
    private boolean hLeg = false;
    private boolean vLeg = false;
    
    private boolean ready = false;

    private int trackedX;
    private int trackedZ;

    private int failedAttempts;

    private final List<String> successes = new ArrayList<>();
    private final List<String> failures = new ArrayList<>();

    private int task = -1;

    public CompactSpiralTask(World world, int radius, FLocation start, FPlayer fPlayer, Faction faction) {
        this.world = world;
        this.limit = (radius - 1) * 2;
        this.trackedX = start.getX();
        this.trackedZ = start.getZ();
        this.fPlayer = fPlayer;
        this.faction = faction;
    }

    public void start() {
        this.ready = true;
        this.task = this.runTaskTimer(FactionsPlugin.getInstance(), 0L, 2L).getTaskId();
    }

    @Override
    public void appendSuccess(String success) {
        this.successes.add(success);
        this.failedAttempts = 0;
    }

    @Override
    public void appendFailure(String failure) {
        if (this.task == -1) {
            return;
        }
        this.failures.add(failure);
        this.failedAttempts++;
        if (this.failedAttempts >= FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit() - 1) {
            this.finish();
        }
    }

    @Override
    public int getSuccessSize() {
        return this.successes.size();
    }

    @Override
    public int getFailureSize() {
        return this.failures.size();
    }

    @Override
    public List<String> getSuccesses() {
        return this.successes;
    }

    @Override
    public List<String> getFailures() {
        return this.failures;
    }

    @Override
    public void run() {
        if (this.task == -1 || !this.ready) {
            return;
        }
        if (this.fPlayer.getFactionIdRaw() != this.faction.getIdRaw()) {
            this.finish();
            return;
        }

        // this is set so it only does one iteration at a time, no matter how frequently the timer fires
        this.ready = false;

        // make sure we're still inside the specified radius
        if (!this.insideRadius()) { //outside the radius
            return;
        }

        // track this to keep one iteration from dragging on too long and possibly choking the system
        long loopStartTime = Instant.now().toEpochMilli();

        // keep going until the task has been running for 20ms or more, then stop to take a breather
        while (Instant.now().toEpochMilli() < loopStartTime + 20) {
            // run the primary task on the current X/Z coordinates
            this.fPlayer.attemptClaimCompact(this.world, this.faction, this.trackedLocation(), this);
            //implement second 'attemptClaim' to actually implement the claiming for the faction.
            if (this.failedAttempts >= FactionsPlugin.getInstance().conf().factions().claims().getRadiusClaimFailureLimit() - 1) {
                this.finish();
                return;
            }

            // move on to next chunk in spiral
            if (!this.retrack()) {
                return;
            }
        }

        // ready for the next iteration to run
        ready = true;
    }

    public final boolean insideRadius() {
        boolean inside = current < limit;
        if (!inside) {
            this.finish();
        }
        return inside;
    }

    private boolean retrack() {
        if (this.task == -1) {
            return false;
        }

        // make sure we don't need to turn down the next leg of the spiral
        if (this.current < this.length) {
            this.current++;

            // if we're outside the radius, we're done
            if (!this.insideRadius()) {
                return false;
            }
        } else {    // one leg/side of the spiral down...
            this.current = 0;
            this.hLeg ^= true;
            // every second leg (between X and Z legs, negative or positive), length increases
            if (this.hLeg) {
                this.vLeg ^= true;
                this.length++;
            }
        }

        // move one chunk further in the appropriate direction
        if (this.hLeg) {
            this.trackedZ += (this.vLeg) ? -1 : 1;
        } else {
            this.trackedX += (this.vLeg) ? -1 : 1;
        }

        return true;
    }

    public FLocation trackedLocation() {
        return FLocation.wrap(this.world.getName(), this.trackedX, this.trackedZ);
    }

    @Override
    public void finish() {
        this.cancel();
        this.ready = false;
        this.task = -1;
        this.onFinish();
    }
}
