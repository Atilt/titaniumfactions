package com.massivecraft.factions.util.formatting.indexed;

import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.formatting.Formattable;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class IndexedFormatter implements Formattable<TL> {

    private static final Pattern PATTERN = Pattern.compile("\\{\\d+}");
    private static final Pattern MESSAGE_FORMAT_SINGLE_QUOTE = Pattern.compile("\\b'\\b");

    @Override
    public boolean contains(String string) {
        return PATTERN.matcher(string).find();
    }

    private final Map<TL, MessageFormat> cache = new EnumMap<>(TL.class);

    @Override
    public String format(TL tl, String... values) {
        return this.cache.get(tl).format(values);
    }

    @Override
    public Component toFormattedComponent(TL tl, String... values) {
        return TextComponent.of(this.format(tl, values));
    }

    @Override
    public TextComponent.Builder toFormattedComponentBuilder(TL tl, String... values) {
        return TextComponent.builder(this.format(tl, values));
    }

    @Override
    public void recache(TL tl, String string) {
        this.cache.put(tl, new MessageFormat(MESSAGE_FORMAT_SINGLE_QUOTE.matcher(string).replaceAll("''")));
    }

    @Override
    public void close() {
        this.cache.clear();
    }
}
