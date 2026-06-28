package com.battlechambers.games;

import com.battlechambers.game.AbstractMiniGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Hot Potato Mini-Game
 * Players pass a hot potato rapidly
 * Last player holding it loses!
 */
public class HotPotato extends AbstractMiniGame {

    public HotPotato() {
        super("hot-potato", "Hot Potato", "Don't hold the potato!", 2, 24, 120);
    }

    @Override
    public void initialize(com.battlechambers.model.Arena arena, Set<Player> players) {
        this.arena = arena;
        for (Player player : players) {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
        }
        
        // Give first player the potato
        Player firstPlayer = players.iterator().next();
        firstPlayer.getInventory().addItem(new ItemStack(Material.BAKED_POTATO));
    }

    @Override
    public void start() {
        // Game started
    }

    @Override
    public void tick(int secondsElapsed) {
        // Game logic per second
    }

    @Override
    public void end() {
        // Game ended
    }

    @Override
    public void onPlayerDeath(Player player, Player killer) {
        gameSession.addDeath(player.getUniqueId());
        gameSession.removePlayer(player.getUniqueId());
    }

    @Override
    public void onPlayerQuit(Player player) {
        gameSession.removePlayer(player.getUniqueId());
    }

    @Override
    public List<Player> getWinners() {
        return List.of();
    }

    @Override
    public String getLeaderboard() {
        return "Hot Potato Leaderboard";
    }
}
