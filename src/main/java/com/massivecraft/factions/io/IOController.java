package com.massivecraft.factions.io;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class IOController {

    private static final Map<String, Lock> LOCKS = new HashMap<>();

    public <T> T read(Path path, Gson gson, Type type) {
        Lock lock = LOCKS.computeIfAbsent(path.getFileName().toString(), s -> new ReentrantReadWriteLock().readLock());
        lock.lock();
        try (Reader reader = Files.newBufferedReader(path, Charsets.UTF_8)) {
            return gson.fromJson(reader, type);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    public <T> void write(Path path, Gson gson, T data, boolean sync, BooleanConsumer finish) {
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
