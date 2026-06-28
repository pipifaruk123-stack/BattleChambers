package com.battlechambers.listener;

import com.battlechambers.BattleChambers;
import com.battlechambers.utils.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerUnloadEvent;

/**
 * Server event listener
 * Handles server startup and shutdown events
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class ServerListener implements Listener {

    private final BattleChambers plugin;

    /**
     * Constructor for ServerListener
     * @param plugin The main plugin instance
     */
    public ServerListener(BattleChambers plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle server startup
     * @param event The ServerLoadEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerLoad(ServerLoadEvent event) {
        Logger.info("Server loaded. BattleChambers plugin ready!");
    }
}
