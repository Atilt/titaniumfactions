package com.massivecraft.factions.util;

import com.massivecraft.factions.math.FastMath;
import net.kyori.text.TextComponent;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
            return Character.toString(this.asciiChar);
        }

        public String getTranslation() {
            if (this == Point.N) {
                return TL.COMPASS_SHORT_NORTH.toString();
            }
            if (this == Point.E) {
                return TL.COMPASS_SHORT_EAST.toString();
            }
            if (this == Point.S) {
                return TL.COMPASS_SHORT_SOUTH.toString();
            }
            if (this == Point.W) {
                return TL.COMPASS_SHORT_WEST.toString();
            }
            return toString();
        }

        public String toString(boolean isActive, ChatColor ACTIVE_COLOR, String colorDefault) {
            return (isActive ? ACTIVE_COLOR : colorDefault) + getTranslation();
        }

        public static Point fromAngle(double degrees) {
            return VALUES[FastMath.round(degrees / 45.0D) & 0x7].getOppositePoint();
        }
    }

    private static final ChatColor ACTIVE_COLOR = ChatColor.RED;
    private static final String DEFAULT_COLOR = TextUtil.parse("<a>");
    
    private static final Map<Point, List<TextComponent>> COMPASSES = new EnumMap<>(Point.class);
    
    static {
        for (Point point : Point.VALUES) {
            List<TextComponent> ret = new ArrayList<>(3);

            StringBuilder builder = new StringBuilder(4);

            builder.append(Point.NW.toString(Point.NW == point, ACTIVE_COLOR, DEFAULT_COLOR))
                    .append(Point.N.toString(Point.N == point, ACTIVE_COLOR, DEFAULT_COLOR))
                    .append(Point.NE.toString(Point.NE == point, ACTIVE_COLOR, DEFAULT_COLOR));
            ret.add(TextUtil.parseFancy(builder.toString()).build());

            builder.setLength(0);
            builder.append(Point.W.toString(Point.W == point, ACTIVE_COLOR, DEFAULT_COLOR))
                    .append(DEFAULT_COLOR).append("+")
                    .append(Point.E.toString(Point.E == point, ACTIVE_COLOR, DEFAULT_COLOR));
            ret.add(TextUtil.parseFancy(builder.toString()).build());

            builder.setLength(0);
            builder.append(Point.SW.toString(Point.SW == point, ACTIVE_COLOR, DEFAULT_COLOR))
                    .append(Point.S.toString(Point.S == point, ACTIVE_COLOR, DEFAULT_COLOR))
                    .append(Point.SE.toString(Point.SE == point, ACTIVE_COLOR, DEFAULT_COLOR));
            ret.add(TextUtil.parseFancy(builder.toString()).build());

            COMPASSES.put(point, ret);
        }
    }

    private static List<TextComponent> get(Point point) {
        return point == null ? Collections.emptyList() : COMPASSES.get(point);
    }

    public static List<TextComponent> get(double degrees) {
        return get(Point.fromAngle(degrees));
    }
}
