package com.massivecraft.factions.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DiscUtil {

    private static final Object2ObjectMap<String, Lock> LOCKS = new Object2ObjectOpenHashMap<>();

    private DiscUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static <T> T read(Path path, Gson gson, Type type) {
        try (Reader reader = Files.newBufferedReader(path, Charsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static <T> void write(Path path, Gson gson, T data, boolean sync, BooleanConsumer finish) {
        Lock lock = LOCKS.computeIfAbsent(path.getFileName().toString(), s -> new ReentrantReadWriteLock().writeLock());
        if (sync) {
            lock.lock();
            try (Writer writer = Files.newBufferedWriter(path, Charsets.UTF_8)) {
                gson.toJson(data, writer);
                if (finish != null) finish.accept(true);
            } catch (IOException exception) {
                exception.printStackTrace();
                if (finish != null) finish.accept(false);
            } finally {
                lock.unlock();
            }
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(FactionsPlugin.getInstance(), () -> {
                lock.lock();
                try (Writer writer = Files.newBufferedWriter(path, Charsets.UTF_8)) {
                    gson.toJson(data, writer);
                    if (finish != null) finish.accept(true);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    if (finish != null) finish.accept(false);
                } finally {
                    lock.unlock();
                }
            });
        }
    }
}
