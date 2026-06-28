package com.battlechambers.listener;

import com.battlechambers.BattleChambers;
import com.battlechambers.model.PlayerData;
import com.battlechambers.utils.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Player event listener
 * Handles player joins, quits, and deaths
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class PlayerListener implements Listener {

    private final BattleChambers plugin;

    /**
     * Constructor for PlayerListener
     * @param plugin The main plugin instance
     */
    public PlayerListener(BattleChambers plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle player join event
     * @param event The PlayerJoinEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Logger.info("Player joined: " + player.getName());

        // Load player data asynchronously
        plugin.getPlayerDataManager().loadPlayer(player);

        // Send welcome message
        player.sendMessage("§a[BattleChambers] Welcome to BattleChambers!");
        player.sendMessage("§7Use §e/bc help §7for commands.");
    }

    /**
     * Handle player quit event
     * @param event The PlayerQuitEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Logger.info("Player quit: " + player.getName());

        // Disconnect player and save data
        plugin.getPlayerDataManager().disconnectPlayer(player.getUniqueId());
    }

    /**
     * Handle player death event
     * @param event The PlayerDeathEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();
        Logger.debug("Player died: " + victim.getName());

        // Update player statistics
        plugin.getPlayerDataManager().getPlayerData(victim.getUniqueId()).addDeath();
        if (killer != null) {
            plugin.getPlayerDataManager().getPlayerData(killer.getUniqueId()).addKill();
            plugin.getPlayerDataManager().addCoins(killer.getUniqueId(), 10);
            killer.sendMessage("§a+10 coins for killing " + victim.getName());
        }
    }
}
