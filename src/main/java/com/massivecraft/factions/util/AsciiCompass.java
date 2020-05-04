package com.massivecraft.factions.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.bukkit.ChatColor;

import java.util.List;

public class AsciiCompass {

    public enum Point {

        N('N'),
        NE('/'),
        E('E'),
        SE('\\'),
        S('S'),
        SW('/'),
        W('W'),
        NW('\\');

        private final char asciiChar;

        public static final Point[] VALUES = values();

        Point(char asciiChar) {
            this.asciiChar = asciiChar;
        }

        public char getAsciiChar() {
            return this.asciiChar;
        }

        public Point getOppositePoint() {
            switch (ordinal()) {
                case 0:
                    return Point.S;
                case 1:
                    return Point.SW;
                case 2:
                    return Point.W;
                case 3:
                    return Point.NW;
                case 5:
                    return Point.NE;
                case 6:
                    return Point.E;
                case 7:
                    return Point.SE;
                default:
                    return Point.N;
            }
        }

        @Override
        public String toString() {
            return String.valueOf(this.asciiChar);
        }

        public String getTranslation() {
            if (this == N) {
                return TL.COMPASS_SHORT_NORTH.toString();
            }
            if (this == E) {
                return TL.COMPASS_SHORT_EAST.toString();
            }
            if (this == S) {
                return TL.COMPASS_SHORT_SOUTH.toString();
            }
            if (this == W) {
                return TL.COMPASS_SHORT_WEST.toString();
            }
            return toString();
        }

        public String toString(boolean isActive, ChatColor colorActive, String colorDefault) {
            return (isActive ? colorActive : colorDefault) + getTranslation();
        }
    }

    public static Point getDirection(float degrees) {
        return Point.VALUES[Math.round(degrees / 45.0f) & 0x7].getOppositePoint();
    }

    public static List<String> getAsciiCompass(Point point, ChatColor colorActive, String colorDefault) {
        if (point == null) {
            return new ObjectArrayList<>(0);
        }
        ObjectList<String> ret = new ObjectArrayList<>(3);

        StringBuilder builder = new StringBuilder();

        builder.append(Point.NW.toString(Point.NW == point, colorActive, colorDefault))
                .append(Point.N.toString(Point.N == point, colorActive, colorDefault))
                .append(Point.NE.toString(Point.NE == point, colorActive, colorDefault));
        ret.add(builder.toString());

        builder.append(Point.W.toString(Point.W == point, colorActive, colorDefault))
                .append(colorDefault).append("+")
                .append(Point.E.toString(Point.E == point, colorActive, colorDefault));
        ret.add(builder.toString());
        
        builder.append(Point.SW.toString(Point.SW == point, colorActive, colorDefault))
                .append(Point.S.toString(Point.S == point, colorActive, colorDefault))
                .append(Point.SE.toString(Point.SE == point, colorActive, colorDefault));
        ret.add(builder.toString());
        return ret;
    }

    public static List<String> getAsciiCompass(float degrees, ChatColor colorActive, String colorDefault) {
        return getAsciiCompass(getDirection(degrees), colorActive, colorDefault);
    }
}
