package com.massivecraft.factions.struct.reserves;

public class Reserve {

    private final String name;
    private final String tag;

    public Reserve(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }
}
