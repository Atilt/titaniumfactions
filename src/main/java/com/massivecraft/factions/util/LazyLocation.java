package com.massivecraft.factions.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/*
 * This class provides a lazy-load Location, so that World doesn't need to be initialized
 * yet when an object of this class is created, only when the Location is first accessed.
 */

public class LazyLocation implements Serializable {
    private static final long serialVersionUID = -6049901271320963314L;
    private transient Location location = null;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public static class Builder {
        private String worldName;
        private double x;
        private double y;
        private double z;
        private float pitch;
        private float yaw;

        public Builder worldName(String worldName) {
            this.worldName = worldName;
            return this;
        }

        public Builder withX(double x) {
            this.x = x;
            return this;
        }

        public Builder withY(double y) {
            this.y = y;
            return this;
        }

        public Builder withZ(double z) {
            this.z = z;
            return this;
        }

        public Builder pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Builder yaw(float yaw) {
            this.yaw = yaw;
            return this;
        }

        public LazyLocation build() {
            return new LazyLocation(this.worldName, this.x, this.y, this.z, this.yaw, this.pitch);
        }
    }

    public LazyLocation(Location loc) {
        setLocation(loc);
    }

    public LazyLocation(final String worldName, final double x, final double y, final double z) {
        this(worldName, x, y, z, 0, 0);
    }

    public LazyLocation(final String worldName, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // This returns the actual Location
    public final Location getLocation() {
        // make sure Location is initialized before returning it
        initLocation();
        return location;
    }

    // change the Location
    public final void setLocation(Location loc) {
        this.location = loc;
        this.worldName = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.yaw = loc.getYaw();
        this.pitch = loc.getPitch();
    }


    // This initializes the Location
    private void initLocation() {
        // if location is already initialized, simply return
        if (location != null) {
            return;
        }

        // get World; hopefully it's initialized at this point
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return;
        }

        // store the Location for future calls, and pass it on
        location = new Location(world, x, y, z, yaw, pitch);
    }


    public final String getWorldName() {
        return worldName;
    }

    public final double getX() {
        return x;
    }

    public final double getY() {
        return y;
    }

    public final double getZ() {
        return z;
    }

    public final double getPitch() {
        return pitch;
    }

    public final double getYaw() {
        return yaw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LazyLocation that = (LazyLocation) o;

        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (Double.compare(that.z, z) != 0) return false;
        if (Float.compare(that.pitch, pitch) != 0) return false;
        if (Float.compare(that.yaw, yaw) != 0) return false;
        return worldName.equals(that.worldName);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }
}
