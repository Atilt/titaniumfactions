package com.massivecraft.factions.util;

import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

public class DiscUtil {

    // -------------------------------------------- //
    // BYTE
    // -------------------------------------------- //


    public static byte[] readBytes(File file) throws IOException {
        int length = (int) file.length();
        byte[] output = new byte[length];
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            int offset = 0;
            while (offset < length) {
                offset += in.read(output, offset, (length - offset));
            }
        }
        return output;
    }

    public static void writeBytes(File file, byte[] bytes) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            out.write(bytes);
        }
    }

    // -------------------------------------------- //
    // STRING
    // -------------------------------------------- //

    public static void write(File file, String content) throws IOException {
        writeBytes(file, content.getBytes(StandardCharsets.UTF_8));
    }

    public static String read(File file) throws IOException {
        return new String(readBytes(file), StandardCharsets.UTF_8);
    }

    // -------------------------------------------- //
    // CATCH
    // -------------------------------------------- //

    private static HashMap<String, Lock> locks = new HashMap<>();

    public static boolean writeCatch(final File file, final String content, boolean sync, BooleanConsumer finish) {
        String name = file.getName();

        // Create lock for each file if there isn't already one.

        Lock lock = locks.computeIfAbsent(name, s -> new ReentrantReadWriteLock().writeLock());

        if (sync) {
            lock.lock();
            try {
                write(file, content);
                if (finish != null) finish.accept(true);
            } catch (IOException e) {
                FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to write file " + file.getAbsolutePath(), e);
                if (finish != null) finish.accept(false);
            } finally {
                lock.unlock();
            }
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        write(file, content);
                        if (finish != null) finish.accept(true);
                    } catch (IOException e) {
                        FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to write file " + file.getAbsolutePath(), e);
                        if (finish != null) finish.accept(false);
                    } finally {
                        lock.unlock();
                    }
                }
            }.runTaskAsynchronously(FactionsPlugin.getInstance());
        }

        return true; // don't really care but for some reason this is a boolean.
    }

    public static String readCatch(File file) {
        try {
            return read(file);
        } catch (IOException e) {
            return null;
        }
    }
}
