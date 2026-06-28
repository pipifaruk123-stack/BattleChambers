package com.battlechambers.listener;

import com.battlechambers.BattleChambers;
import com.battlechambers.utils.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * World event listener
 * Handles world-related events
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class WorldListener implements Listener {

    private final BattleChambers plugin;

    /**
     * Constructor for WorldListener
     * @param plugin The main plugin instance
     */
    public WorldListener(BattleChambers plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle world load event
     * @param event The WorldLoadEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        Logger.debug("World loaded: " + event.getWorld().getName());
    }
}
