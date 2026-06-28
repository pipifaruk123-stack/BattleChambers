package com.battlechambers.games;

import com.battlechambers.game.AbstractMiniGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Block Party Mini-Game
 * Blocks disappear and players must stand on the correct ones
 * Last player standing wins!
 */
public class BlockParty extends AbstractMiniGame {

    public BlockParty() {
        super("block-party", "Block Party", "Don't fall!", 2, 24, 120);
    }

    @Override
    public void initialize(com.battlechambers.model.Arena arena, Set<Player> players) {
        this.arena = arena;
        for (Player player : players) {
            player.getInventory().clear();
            player.setHealth(20);
            player.setFoodLevel(20);
        }
    }

    @Override
    public void start() {
        // Game started
    }

    @Override
    public void tick(int secondsElapsed) {
        // Remove blocks at intervals
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
        return "Block Party Leaderboard";
    }
}
