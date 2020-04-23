package com.massivecraft.factions.util;

import com.google.gson.Gson;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DiscUtil {

    private static final Object2ObjectMap<String, Lock> LOCKS = new Object2ObjectOpenHashMap<>();

    private DiscUtil() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

    public static String read(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    public static <T> void writeCatch(File file, Gson gson, T data, boolean sync, BooleanConsumer finish) {
        Lock lock = LOCKS.computeIfAbsent(file.getName(), s -> new ReentrantReadWriteLock().writeLock());
        if (sync) {
            lock.lock();
            try (Writer writer = new FileWriter(file)) {
                if (!file.exists()) {
                    file.createNewFile();
                }
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
                try (Writer writer = new FileWriter(file)) {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
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

    public static String readCatch(File file) {
        try {
            return read(file);
        } catch (IOException e) {
            return null;
        }
    }
}
