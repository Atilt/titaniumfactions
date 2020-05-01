package com.massivecraft.factions.util.particle;

import com.darkblade12.particleeffect.ParticleEffect;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectRBTreeMap;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketParticleProvider implements ParticleProvider<ParticleEffect> {

    private final Object2ObjectMap<String, ParticleEffect> cache = new Object2ObjectRBTreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final ParticleEffect[] VALUES = ParticleEffect.values();

    @Override
    public String name() {
        return "PACKETS";
    }


    @Override
    public void spawn(ParticleEffect particleEffect, Location location, int count) {
        particleEffect.display(0, 0, 0, 1, count, location, (List<Player>) Bukkit.getOnlinePlayers());
    }

    @Override
    public void playerSpawn(Player player, ParticleEffect particleEffect, Location location, int count) {
        particleEffect.display(0, 0, 0, 1, count, location, player);
    }

    @Override
    public void playerSpawn(Player player, ParticleEffect particleEffect, int count, Location... locations) {
        for (Location location : locations) {
            playerSpawn(player, particleEffect, location, count);
        }
    }

    @Override
    public void playerSpawn(Player player, ParticleEffect particleEffect, Color color, Location... locations) {
        for (Location location : locations) {
            playerSpawn(player, particleEffect, location, color);
        }
    }

    @Override
    public void spawn(ParticleEffect particleEffect, Location location, int count, double speed, double offsetX, double offsetY, double offsetZ) {
        particleEffect.display((float) offsetX, (float) offsetY, (float) offsetZ, (float) speed, count, location, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    @Override
    public void playerSpawn(Player player, ParticleEffect particleEffect, Location location, int count, double speed, double offsetX, double offsetY, double offsetZ) {
        particleEffect.display((float) offsetX, (float) offsetY, (float) offsetZ, (float) speed, count, location, player);
    }

    @Override
    public void spawn(ParticleEffect particleEffect, Location location, Color color) {
        spawn(particleEffect, location, 0, 1, ParticleColor.getOffsetX(color), ParticleColor.getOffsetY(color), ParticleColor.getOffsetZ(color));
    }

    @Override
    public void playerSpawn(Player player, ParticleEffect particleEffect, Location location, Color color) {
        playerSpawn(player, particleEffect, location, 0, 1, ParticleColor.getOffsetX(color), ParticleColor.getOffsetY(color), ParticleColor.getOffsetZ(color));
    }

    @Override
    public ParticleEffect effectFromString(String string) {
        return cache.computeIfAbsent(string, name -> {
            for (ParticleEffect particle : VALUES) {
                if (particle.name().equalsIgnoreCase(name)) {
                    return particle;
                }
            }
            return ParticleEffect.fromName(name);
        });
    }

    @Override
    public String effectName(ParticleEffect particleEffect) {
        return particleEffect.name();
    }

}
