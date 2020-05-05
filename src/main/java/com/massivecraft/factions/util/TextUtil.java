package com.massivecraft.factions.util;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FactionsPlugin;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.kyori.text.TextComponent;
import net.kyori.text.format.Style;
import net.kyori.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtil {

    private static final Object2ObjectMap<String, String> TAGS = new Object2ObjectOpenHashMap<>();
    private static final Map<String, String> RAW_TAGS = new LinkedHashMap<>();
    
    static {
        RAW_TAGS.put("l", "<green>"); // logo
        RAW_TAGS.put("a", "<gold>"); // art
        RAW_TAGS.put("n", "<silver>"); // notice
        RAW_TAGS.put("i", "<yellow>"); // info
        RAW_TAGS.put("g", "<lime>"); // good
        RAW_TAGS.put("b", "<rose>"); // bad
        RAW_TAGS.put("h", "<pink>"); // highligh
        RAW_TAGS.put("c", "<aqua>"); // command
        RAW_TAGS.put("p", "<teal>"); // parameter
    }
    
    public static void init() {
        Map<String, String> tagsFromFile = FactionsPlugin.getInstance().getPersist().load(new TypeToken<Map<String, String>>(){}.getType(), "tags");
        if (tagsFromFile != null) {
            RAW_TAGS.putAll(tagsFromFile);
        }
        FactionsPlugin.getInstance().getPersist().save(RAW_TAGS, "tags");

        for (Map.Entry<String, String> rawTag : RAW_TAGS.entrySet()) {
            TAGS.put(rawTag.getKey(), TextUtil.parseColor(rawTag.getValue()));
        }
    }

    public String put(String key, String value) {
        return TAGS.put(key, value);
    }

    public static String parse(String str, Object... args) {
        return String.format(parse(str), args);
    }

    public static String parse(String str) {
        return parseTags(parseColor(str));
    }
    public static String parseTags(String str) {
        return replaceTags(str, TAGS);
    }

    public static final transient Pattern PATTERN_TAG = Pattern.compile("<([a-zA-Z0-9_]*)>");

    public static String replaceTags(String str, Map<String, String> tags) {
        StringBuffer ret = new StringBuffer();
        Matcher matcher = PATTERN_TAG.matcher(str);
        while (matcher.find()) {
            String tag = matcher.group(1);
            String repl = tags.get(tag);
            matcher.appendReplacement(ret, repl == null ? "<" + tag + ">" : repl);
        }
        matcher.appendTail(ret);
        return ret.toString();
    }

    // -------------------------------------------- //
    // Fancy parsing
    // -------------------------------------------- //

    public static TextComponent parseFancy(String prefix) {
        return toFancy(parse(prefix));
    }

    private static final Map<ChatColor, TextColor> BUKKIT_TO_KYORI = new EnumMap<>(ChatColor.class);

    static {
        for (ChatColor chatColor : ChatColor.values()) {
            if (chatColor.isColor()) {
                BUKKIT_TO_KYORI.put(chatColor, TextColor.valueOf(chatColor.name()));
            }
        }
    }

    public static TextColor kyoriColor(ChatColor chatColor) {
        return BUKKIT_TO_KYORI.get(chatColor);
    }

    private static TextComponent toFancy(String first) {
        String text = "";
        TextComponent message = TextComponent.of(text);
        ChatColor color = null;
        char[] chars = first.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            TextComponent textComponent = TextComponent.of(text);
            if (chars[i] == '§') {
                if (color != null) {
                    TextColor textColor = kyoriColor(color);
                    if (color.isColor()) {
                        message.append(textComponent).color(textColor);
                    } else {
                        message.append(textComponent).style(Style.of(textColor));
                    }
                    text = "";
                }
                color = ChatColor.getByChar(chars[i + 1]);
                i++; // skip color char
            } else {
                text += chars[i];
            }
        }
        TextColor textColor = kyoriColor(color);
        if (text.length() > 0) {
            TextComponent textComponent = TextComponent.of(text);
            if (color != null) {
                if (color.isColor()) {
                    message.append(textComponent).color(textColor);
                } else {
                    message.append(textComponent).style(Style.of(textColor));
                }
            } else {
                message.content(text);
            }
        }
        return message;
    }

    // -------------------------------------------- //
    // Color parsing
    // -------------------------------------------- //

    public static String parseColorBukkit(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String parseColor(String string) {
        return parseColorTags(parseColorAcc(parseColorBukkit(string)));
    }

    @Deprecated
    public static String parseColorAmp(String string) {
        return parseColorBukkit(string);
    }

    public static String parseColorAcc(String string) {
        return string.replace("`e", "").replace("`r", ChatColor.RED.toString()).replace("`R", ChatColor.DARK_RED.toString()).replace("`y", ChatColor.YELLOW.toString()).replace("`Y", ChatColor.GOLD.toString()).replace("`g", ChatColor.GREEN.toString()).replace("`G", ChatColor.DARK_GREEN.toString()).replace("`a", ChatColor.AQUA.toString()).replace("`A", ChatColor.DARK_AQUA.toString()).replace("`b", ChatColor.BLUE.toString()).replace("`B", ChatColor.DARK_BLUE.toString()).replace("`p", ChatColor.LIGHT_PURPLE.toString()).replace("`P", ChatColor.DARK_PURPLE.toString()).replace("`k", ChatColor.BLACK.toString()).replace("`s", ChatColor.GRAY.toString()).replace("`S", ChatColor.DARK_GRAY.toString()).replace("`w", ChatColor.WHITE.toString());
    }

    public static String parseColorTags(String string) {
        return string.replace("<empty>", "").replace("<black>", "\u00A70").replace("<navy>", "\u00A71").replace("<green>", "\u00A72").replace("<teal>", "\u00A73").replace("<red>", "\u00A74").replace("<purple>", "\u00A75").replace("<gold>", "\u00A76").replace("<silver>", "\u00A77").replace("<gray>", "\u00A78").replace("<blue>", "\u00A79").replace("<lime>", "\u00A7a").replace("<aqua>", "\u00A7b").replace("<rose>", "\u00A7c").replace("<pink>", "\u00A7d").replace("<yellow>", "\u00A7e").replace("<white>", "\u00A7f");
    }
    
    public static String replace(String string, String search, String replacement) {
        return StringUtils.replace(string, search, replacement);
    }

    // -------------------------------------------- //
    // Standard utils like UCFirst, implode and repeat.
    // -------------------------------------------- //

    public static String upperCaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String implode(List<String> list, String glue) {
        return String.join(glue, list);
    }

    public static String repeat(String s, int times) {
        return times > 0 ? s + repeat(s, times - 1) : "";
    }

    // -------------------------------------------- //
    // Material name tools
    // -------------------------------------------- //

    public static String getMaterialName(Material material) {
        return material.toString().replace('_', ' ').toLowerCase();
    }

    // -------------------------------------------- //
    // Paging and chrome-tools like titleize
    // -------------------------------------------- //

    private final static String titleizeLine = repeat("_", 52);
    private final static int titleizeBalance = -1;

    public static String titleize(String str) {
        String center = ".[ " + parseTags("<l>") + str + parseTags("<a>") + " ].";
        int centerlen = ChatColor.stripColor(center).length();
        int pivot = titleizeLine.length() / 2;
        int eatLeft = (centerlen / 2) - titleizeBalance;
        int eatRight = (centerlen - eatLeft) + titleizeBalance;

        if (eatLeft < pivot) {
            return parseTags("<a>") + titleizeLine.substring(0, pivot - eatLeft) + center + titleizeLine.substring(pivot + eatRight);
        } else {
            return parseTags("<a>") + center;
        }
    }

    public static List<String> getPage(List<String> lines, int pageHumanBased, String title) {
        int pageZeroBased = pageHumanBased - 1;
        int pagecount = (lines.size() / 9) + 1;

        ObjectList<String> ret = new ObjectArrayList<>(pagecount);

        ret.add(titleize(title + " " + pageHumanBased + "/" + pagecount));

        if (pageZeroBased < 0 || pageHumanBased > pagecount) {
            ret.add(parseTags(TL.INVALIDPAGE.format(pagecount)));
            return ret;
        }

        int from = pageZeroBased * 9;
        ret.addAll(lines.subList(from, Math.min(from + 9, lines.size())));
        return ret;
    }
}
