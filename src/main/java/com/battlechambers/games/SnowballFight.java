package com.battlechambers.games;

import com.battlechambers.game.AbstractMiniGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Snowball Fight Mini-Game
 * Players fight with snowballs
 * Last player standing wins!
 */
public class SnowballFight extends AbstractMiniGame {

    public SnowballFight() {
        super("snowball-fight", "Snowball Fight", "Throw snowballs!", 2, 24, 120);
    }

    @Override
    public void initialize(com.battlechambers.model.Arena arena, Set<Player> players) {
        this.arena = arena;
        for (Player player : players) {
            player.getInventory().clear();
            player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 64));
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
        // Game logic per second
    }

    @Override
    public void end() {
        // Game ended
    }

    @Override
    public void onPlayerDeath(Player player, Player killer) {
        if (killer != null) {
            gameSession.addKill(killer.getUniqueId());
            gameSession.addScore(killer.getUniqueId(), 10);
        }
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
        return "Snowball Fight Leaderboard";
    }
}
