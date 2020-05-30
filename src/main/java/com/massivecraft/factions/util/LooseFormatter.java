package com.massivecraft.factions.util;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.EnumMap;
import java.util.Map;

public final class LooseFormatter implements Formattable<TL> {

    private final Map<TL, CompiledStringFormat> cache = new EnumMap<>(TL.class);

    @Override
    public boolean contains(String string) {
        return string.contains("[]");
    }

    @Override
    public String format(TL tl, String... values) {
        return this.cache.get(tl).formatStrings(values);
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
        this.cache.put(tl, StringFormat.compile(string));
    }

    @Override
    public void close() {
        this.cache.clear();
    }
}
