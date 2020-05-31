package com.massivecraft.factions.util.particle;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class BukkitParticleProvider implements ParticleProvider<Particle> {

    @Override
    public String name() {
        return "BUKKIT";
    }

    @Override
    public void spawn(Particle effect, Location location, int count) {
        location.getWorld().spawnParticle(effect, location, count);
    }

    @Override
    public void playerSpawn(Player player, Particle effect, Location location, int count) {
        player.spawnParticle(effect, location, count);
    }

    @Override
    public void playerSpawn(Player player, Particle particle, int count, Location... locations) {
        for (Location location : locations) {
            playerSpawn(player, particle, location, count);
        }
    }

    @Override
    public void spawn(Particle particle, Location location, int count, double speed, double offsetX, double offsetY, double offsetZ) {
        location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void playerSpawn(Player player, Particle particle, Location location, int count, double speed, double offsetX, double offsetY, double offsetZ) {
        player.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void spawn(Particle particle, Location location, Color color) {
        location.getWorld().spawnParticle(particle, location, 1, new Particle.DustOptions(color, 1));
    }

    @Override
    public void playerSpawn(Player player, Particle particle, Location location, Color color) {
        player.spawnParticle(particle, location, 1, new Particle.DustOptions(color, 1.5f));
    }

    @Override
    public void playerSpawn(Player player, Particle particle, Color color, Location... locations) {
        for (Location location : locations) {
            playerSpawn(player, particle, location, color);
        }
    }

    @Override
    public Particle effectFromString(String string) {
        for (Particle particle : Particle.values()) {
            if (particle.name().equalsIgnoreCase(string)) {
                return particle;
            }
        }
        return null;
    }

    @Override
    public String effectName(Particle particle) {
        return particle.name();
    }
}