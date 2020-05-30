package com.massivecraft.factions.util;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

public interface Formattable<T> extends AutoCloseable {

    boolean contains(String string);

    String format(T t, String... values);

    Component toFormattedComponent(T t, String... values);

    TextComponent.Builder toFormattedComponentBuilder(T t, String... values);

    void recache(T t, String string);
}
