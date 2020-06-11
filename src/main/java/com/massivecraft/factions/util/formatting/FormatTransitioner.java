package com.massivecraft.factions.util.formatting;

import com.google.common.base.Charsets;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.formatting.loose.StringFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public final class FormatTransitioner {

    private static FormatTransitioner instance;

    public static FormatTransitioner get() {
        if (instance == null) {
            synchronized (FormatTransitioner.class) {
                if (instance == null) {
                    instance = new FormatTransitioner();
                }
            }
        }
        return instance;
    }

    private static final List<String> OLD_FORMATS = new ArrayList<>(23);

    static {
        OLD_FORMATS.add("%1$s");
        OLD_FORMATS.add("%2$s");
        OLD_FORMATS.add("%3$s");
        OLD_FORMATS.add("%4$s");
        OLD_FORMATS.add("%5$s");
        OLD_FORMATS.add("%6$s");
        OLD_FORMATS.add("%s");

        OLD_FORMATS.add("%1$d");
        OLD_FORMATS.add("%2$d");
        OLD_FORMATS.add("%3$d");
        OLD_FORMATS.add("%d");

        OLD_FORMATS.add("%1$f");
        OLD_FORMATS.add("%1$f");
        OLD_FORMATS.add("%.2f");

        OLD_FORMATS.add("{0}");
        OLD_FORMATS.add("{1}");
        OLD_FORMATS.add("{2}");
        OLD_FORMATS.add("{3}");
        OLD_FORMATS.add("{4}");
        OLD_FORMATS.add("{5}");
        OLD_FORMATS.add("{6}");
    }

    private FormatTransitioner() {}

    public boolean toLoose(Path indexed) {
        try {
            List<String> lines = Files.readAllLines(indexed, Charsets.UTF_8);
            ListIterator<String> iterator = lines.listIterator();

            while (iterator.hasNext()) {
                String current = iterator.next();
                for (String oldFormat : OLD_FORMATS) {
                    if (current.contains(oldFormat)) {
                        current = TextUtil.replace(current, oldFormat, StringFormat.DELIMITER);
                    }
                }
                iterator.set(current);
            }
            Files.write(indexed, lines, Charsets.UTF_8);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toIndexed(Path indexed) {
        try {
            List<String> lines = Files.readAllLines(indexed, Charsets.UTF_8);
            ListIterator<String> iterator = lines.listIterator();

            while (iterator.hasNext()) {
                String current = iterator.next();
                for (String oldFormat : OLD_FORMATS) {
                    if (current.contains(oldFormat)) {
                        current = TextUtil.replace(current, oldFormat, StringFormat.DELIMITER);
                    }
                }
                StringBuilder rebuilt = new StringBuilder(current.length() + 20);

                char[] chars = current.toCharArray();

                int currentIndex = 0;

                boolean startTag = false;

                for (int i = 0; i < chars.length; i++) {
                    char aChar = chars[i];
                    if (!startTag && i != chars.length - 1 && aChar == '[') {
                        startTag = true;
                        continue;
                    }
                    if (startTag) {
                        if (aChar == ']') {
                            rebuilt.append('{').append(currentIndex++).append('}');
                            startTag = false;
                            continue;
                        } else {
                            rebuilt.append('[');
                        }
                    }
                    startTag = false;
                    rebuilt.append(aChar);
                }
                iterator.set(rebuilt.toString());
            }
            Files.write(indexed, lines, Charsets.UTF_8);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }
}
