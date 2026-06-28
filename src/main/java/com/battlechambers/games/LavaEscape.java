package com.battlechambers.games;

import com.battlechambers.game.AbstractMiniGame;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Lava Escape Mini-Game
 * Players must escape rising lava
 * Last player standing wins!
 */
public class LavaEscape extends AbstractMiniGame {

    public LavaEscape() {
        super("lava-escape", "Lava Escape", "Escape the lava!", 2, 24, 120);
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
        // Raise lava level
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
        return "Lava Escape Leaderboard";
    }
}
