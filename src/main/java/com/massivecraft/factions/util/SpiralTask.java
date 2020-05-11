package com.massivecraft.factions.util;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.logging.Level;


/*
 * reference diagram, task should move in this pattern out from chunk 0 in the center.
 *  8 [>][>][>][>][>] etc.
 * [^][6][>][>][>][>][>][6]
 * [^][^][4][>][>][>][4][v]
 * [^][^][^][2][>][2][v][v]
 * [^][^][^][^][0][v][v][v]
 * [^][^][^][1][1][v][v][v]
 * [^][^][3][<][<][3][v][v]
 * [^][5][<][<][<][<][5][v]
 * [7][<][<][<][<][<][<][7]
 */

public abstract class SpiralTask implements Runnable {

    // general task-related reference data
    private transient World world;
    private transient boolean readyToGo = false;
    private transient int task = -1;
    private transient int limit;

    // values for the spiral pattern routine
    private transient int x = 0;
    private transient int z = 0;
    private transient boolean isZLeg = false;
    private transient boolean isNeg = false;
    private transient int length = -1;
    private transient int current = 0;

    private final Runnable finish;

    public SpiralTask(FLocation fLocation, int radius, Runnable finish) {
        // limit is determined based on spiral leg length for given radius; see insideRadius()
        this.limit = (radius - 1) * 2;

        this.world = Bukkit.getWorld(fLocation.getWorldName());
        this.finish = finish;
        if (this.world == null) {
            FactionsPlugin.getInstance().log(Level.WARNING, "[SpiralTask] A valid world must be specified!");
            this.stop();
            if (finish != null) {
                finish.run();
            }
            return;
        }

        this.x = fLocation.getX();
        this.z = fLocation.getZ();

        this.readyToGo = true;

        // get this party started
        this.setTask(Bukkit.getScheduler().runTaskTimer(FactionsPlugin.getInstance(), this, 2, 2).getTaskId());
    }

    /*
     * This is where the necessary work is done; you'll need to override this method with whatever you want
     * done at each chunk in the spiral pattern.
     * Return false if the entire task needs to be aborted, otherwise return true to continue.
     */
    public abstract boolean work();

    /*
     * Returns an FLocation pointing at the current chunk X and Z values.
     */
    public final FLocation currentFLocation() {
        return FLocation.wrap(world.getName(), x, z);
    }

    /*
     * Returns a Location pointing at the current chunk X and Z values.
     * note that the Location is at the corner of the chunk, not the center.
     */
    public final Location currentLocation() {
        return new Location(world, FLocation.chunkToBlock(x), 65.0, FLocation.chunkToBlock(z));
    }

    /*
     * Returns current chunk X and Z values.
     */
    public final int getX() {
        return x;
    }

    public final int getZ() {
        return z;
    }



    /*
     * Below are the guts of the class, which you normally wouldn't need to mess with.
     */

    public final void setTask(int ID) {
        if (ID == -1) {
            this.stop();
        }
        this.task = ID;
    }

    public final void run() {
        if (!this.valid() || !readyToGo) {
            return;
        }

        // this is set so it only does one iteration at a time, no matter how frequently the timer fires
        readyToGo = false;

        // make sure we're still inside the specified radius
        if (!this.insideRadius()) {
            return;
        }

        // track this to keep one iteration from dragging on too long and possibly choking the system
        long loopStartTime = now();

        // keep going until the task has been running for 20ms or more, then stop to take a breather
        while (now() < loopStartTime + 20) {
            // run the primary task on the current X/Z coordinates
            if (!this.work()) {
                this.finish();
                return;
            }

            // move on to next chunk in spiral
            if (!this.moveToNext()) {
                return;
            }
        }

        // ready for the next iteration to run
        readyToGo = true;
    }

    // step through chunks in spiral pattern from center; returns false if we're done, otherwise returns true
    public final boolean moveToNext() {
        if (!this.valid()) {
            return false;
        }

        // make sure we don't need to turn down the next leg of the spiral
        if (current < length) {
            current++;

            // if we're outside the radius, we're done
            if (!this.insideRadius()) {
                return false;
            }
        } else {    // one leg/side of the spiral down...
            current = 0;
            isZLeg ^= true;
            // every second leg (between X and Z legs, negative or positive), length increases
            if (isZLeg) {
                isNeg ^= true;
                length++;
            }
        }

        // move one chunk further in the appropriate direction
        if (isZLeg) {
            z += (isNeg) ? -1 : 1;
        } else {
            x += (isNeg) ? -1 : 1;
        }

        return true;
    }

    public final boolean insideRadius() {
        boolean inside = current < limit;
        if (!inside) {
            this.finish();
        }
        return inside;
    }

    // for successful completion
    public void finish() {
//		P.getInstance().log("SpiralTask successfully completed!");
        this.stop();
        if (this.finish != null) {
            this.finish.run();
        }
    }

    // we're done, whether finished or cancelled
    public final void stop() {
        if (!this.valid()) {
            return;
        }
        readyToGo = false;
        Bukkit.getScheduler().cancelTask(this.task);
        this.task = -1;
    }

    // is this task still valid/workable?
    public final boolean valid() {
        return task != -1;
    }

    private static long now() {
        return System.currentTimeMillis();
    }
}
