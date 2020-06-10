package com.massivecraft.factions;

public interface Trackable<T> {

    boolean track(T t);

    boolean untrack(T t);
}
