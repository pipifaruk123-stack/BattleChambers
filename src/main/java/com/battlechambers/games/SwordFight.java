package com.battlechambers.games;

import com.battlechambers.game.AbstractMiniGame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

/**
 * Sword Fight Mini-Game
 * Players engage in melee combat with swords
 * Last player standing wins!
 */
public class SwordFight extends AbstractMiniGame {

    public SwordFight() {
        super("sword-fight", "Sword Fight", "Duel with swords!", 2, 24, 120);
    }

    @Override
    public void initialize(com.battlechambers.model.Arena arena, Set<Player> players) {
        this.arena = arena;
        for (Player player : players) {
            player.getInventory().clear();
            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            player.getInventory().addItem(sword);
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
        return "Sword Fight Leaderboard";
    }
}
