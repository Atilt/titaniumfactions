package com.massivecraft.factions.util.particle;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public final class ParticleColor {

    private ParticleColor() {
        throw new UnsupportedOperationException("this class cannot be instantiated");
    }
    
    public static float getOffsetX(Color color) {
        return color.getRed() == 0 ? Float.MIN_VALUE : color.getRed() / 255.0F;
    }

    public static float getOffsetY(Color color) {
        return color.getGreen() / 255.0F;
    }

    public static float getOffsetZ(Color color) {
        return color.getBlue() / 255.0F;
    }
    
    public static Color fromChatColor(ChatColor chatColor) {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
            case DARK_AQUA:
            case DARK_BLUE:
                return Color.BLUE;
            case DARK_GRAY:
            case GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_PURPLE:
            case LIGHT_PURPLE:
                return Color.PURPLE;
            case DARK_RED:
            case RED:
                return Color.RED;
            case GOLD:
            case YELLOW:
                return Color.YELLOW;
            case GREEN:
                return Color.LIME;
            case WHITE:
                return Color.WHITE;
            default:
                break;
        }
        return null;
    }
}
