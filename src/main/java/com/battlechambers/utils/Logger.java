package com.battlechambers.utils;

import org.bukkit.Bukkit;

/**
 * Utility class for logging messages to console
 * Provides colored logging with prefix
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class Logger {

    private static final String PREFIX = "§6[BattleChambers]§r ";
    private static final String ERROR_PREFIX = "§c[BattleChambers ERROR]§r ";
    private static final String WARN_PREFIX = "§e[BattleChambers WARN]§r ";
    private static final String DEBUG_PREFIX = "§7[BattleChambers DEBUG]§r ";

    /**
     * Log info message
     * @param message The message to log
     */
    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + message);
    }

    /**
     * Log error message
     * @param message The error message
     */
    public static void error(String message) {
        Bukkit.getConsoleSender().sendMessage(ERROR_PREFIX + message);
    }

    /**
     * Log warning message
     * @param message The warning message
     */
    public static void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(WARN_PREFIX + message);
    }

    /**
     * Log debug message
     * @param message The debug message
     */
    public static void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(DEBUG_PREFIX + message);
    }
}
