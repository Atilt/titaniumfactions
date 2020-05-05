package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

// TODO: Give better name and place to differentiate from the entity-orm-ish system in "com.massivecraft.core.persist".

public class Persist {

    // ------------------------------------------------------------ //
    // GET NAME - What should we call this type of object?
    // ------------------------------------------------------------ //

    public static String getName(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String getName(Object o) {
        return getName(o.getClass());
    }

    public static String getName(Type type) {
        return getName(type.getClass());
    }

    // ------------------------------------------------------------ //
    // GET Path - In which Path would we like to store this object?
    // ------------------------------------------------------------ //

    public Path getPath(String name) {
        return FactionsPlugin.getInstance().getDataFolder().toPath().resolve(name + ".json");
    }

    public Path getPath(Class<?> clazz) {
        return getPath(getName(clazz));
    }

    public Path getPath(Object obj) {
        return getPath(getName(obj));
    }

    public Path getPath(Type type) {
        return getPath(getName(type));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz) {
        return loadOrSaveDefault(def, clazz, getPath(clazz));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, String name) {
        return loadOrSaveDefault(def, clazz, getPath(name));
    }

    public <T> T loadOrSaveDefault(T def, Class<T> clazz, Path path) {
        String name = path.getFileName().toString();
        if (Files.notExists(path)) {
            FactionsPlugin.getInstance().getLogger().info("Creating default: " + name);
            this.save(def, path);
            return def;
        }

        T loaded = this.load(clazz, path);

        if (loaded == null) {
            FactionsPlugin.getInstance().log(Level.WARNING, "Using default as I failed to load: " + name);

            // backup bad Path, so user can attempt to recover their changes from it
            Path backup = path.resolve(name + "_bad");
            try {
                Files.deleteIfExists(backup);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FactionsPlugin.getInstance().log(Level.WARNING, "Backing up copy of bad Path to: " + backup.getFileName().toString());
            try {
                Files.move(path, backup);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return def;
        }
        return loaded;
    }
    public boolean save(Object instance) {
        return save(instance, getPath(instance));
    }

    public boolean save(Object instance, String name) {
        return save(instance, getPath(name));
    }

    public boolean save(Object instance, Path path, BooleanConsumer finish) {
        DiscUtil.write(path, FactionsPlugin.getInstance().getGson(), instance, true, finish);
        return true;
    }

    public boolean save(Object instance, Path path) {
        return save(instance, path, null);
    }

    public <T> T load(Class<T> clazz) {
        return load(clazz, getPath(clazz));
    }

    public <T> T load(Class<T> clazz, String name) {
        return load(clazz, getPath(name));
    }

    public <T> T load(Class<T> clazz, Path path) {
        return DiscUtil.read(path, FactionsPlugin.getInstance().getGson(), clazz);
    }


    @SuppressWarnings("unchecked")
    public <T> T load(Type typeOfT, String name) {
        return (T) load(typeOfT, getPath(name));
    }

    public <T> T load(Type typeOfT, Path path) {
        return DiscUtil.read(path, FactionsPlugin.getInstance().getGson(), typeOfT);
    }
}
