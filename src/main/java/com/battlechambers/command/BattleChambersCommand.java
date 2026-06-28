package com.battlechambers.command;

import com.battlechambers.BattleChambers;
import com.battlechambers.utils.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Main command handler for BattleChambers
 * Handles /bc commands for arena and game management
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class BattleChambersCommand implements CommandExecutor, TabCompleter {

    private final BattleChambers plugin;

    /**
     * Constructor for BattleChambersCommand
     * @param plugin The main plugin instance
     */
    public BattleChambersCommand(BattleChambers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c[BattleChambers] Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "help":
                sendHelp(player);
                return true;
            case "arena":
                handleArenaCommand(player, args);
                return true;
            case "game":
                handleGameCommand(player, args);
                return true;
            case "join":
                handleJoinCommand(player, args);
                return true;
            case "leave":
                handleLeaveCommand(player);
                return true;
            case "stats":
                handleStatsCommand(player);
                return true;
            case "top":
                handleTopCommand(player, args);
                return true;
            default:
                player.sendMessage("§c[BattleChambers] Unknown command. Use /bc help");
                return true;
        }
    }

    /**
     * Send help message
     * @param player The player
     */
    private void sendHelp(Player player) {
        player.sendMessage("\n§6===== §lBattleChambers§r §6=====");
        player.sendMessage("§e/bc help §7- Show this help message");
        player.sendMessage("§e/bc arena list §7- List all arenas");
        player.sendMessage("§e/bc game list §7- List all games");
        player.sendMessage("§e/bc join <game> §7- Join a game");
        player.sendMessage("§e/bc leave §7- Leave current game");
        player.sendMessage("§e/bc stats §7- View your statistics");
        player.sendMessage("§e/bc top <stat> §7- View top players");
        player.sendMessage("§6========================\n");
    }

    /**
     * Handle arena command
     * @param player The player
     * @param args Command arguments
     */
    private void handleArenaCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c[BattleChambers] Usage: /bc arena <list|info> [name]");
            return;
        }

        String arenaSubCommand = args[1].toLowerCase();
        if (arenaSubCommand.equals("list")) {
            int count = plugin.getArenaManager().getArenaCount();
            player.sendMessage("§a[BattleChambers] Total arenas: §f" + count);
            for (com.battlechambers.model.Arena arena : plugin.getArenaManager().getAllArenas()) {
                String status = arena.isEnabled() ? "§a✓" : "§c✗";
                player.sendMessage("  " + status + " §e" + arena.getName() + " §7- §f" + arena.getMaxPlayers() + " players");
            }
        }
    }

    /**
     * Handle game command
     * @param player The player
     * @param args Command arguments
     */
    private void handleGameCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c[BattleChambers] Usage: /bc game <list|info> [name]");
            return;
        }

        String gameSubCommand = args[1].toLowerCase();
        if (gameSubCommand.equals("list")) {
            int count = plugin.getGameManager().getGameCount();
            player.sendMessage("§a[BattleChambers] Available games: §f" + count);
            for (com.battlechambers.game.AbstractMiniGame game : plugin.getGameManager().getAllGames()) {
                player.sendMessage("  §e" + game.getGameName() + " §7- §f" + game.getGameDescription());
                player.sendMessage("    §7Players: " + game.getMinPlayers() + "-" + game.getMaxPlayers());
            }
        }
    }

    /**
     * Handle join command
     * @param player The player
     * @param args Command arguments
     */
    private void handleJoinCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c[BattleChambers] Usage: /bc join <game>");
            return;
        }

        String gameName = args[1].toLowerCase();
        com.battlechambers.game.AbstractMiniGame game = plugin.getGameManager().getGame(gameName);

        if (game == null) {
            player.sendMessage("§c[BattleChambers] Game not found: " + gameName);
            return;
        }

        player.sendMessage("§a[BattleChambers] You joined " + game.getGameName() + "!");
    }

    /**
     * Handle leave command
     * @param player The player
     */
    private void handleLeaveCommand(Player player) {
        player.sendMessage("§a[BattleChambers] You left the game.");
    }

    /**
     * Handle stats command
     * @param player The player
     */
    private void handleStatsCommand(Player player) {
        com.battlechambers.model.PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        if (data == null) {
            player.sendMessage("§c[BattleChambers] No data found.");
            return;
        }

        player.sendMessage("\n§6===== §lYour Statistics§r §6=====");
        player.sendMessage("§eLevel: §f" + data.getLevel());
        player.sendMessage("§eXP: §f" + data.getXp());
        player.sendMessage("§eCoins: §f" + data.getCoins());
        player.sendMessage("§eWins: §f" + data.getWins());
        player.sendMessage("§eKills: §f" + data.getKills());
        player.sendMessage("§eDeaths: §f" + data.getDeaths());
        player.sendMessage("§eKDR: §f" + String.format("%.2f", data.getKDR()));
        player.sendMessage("§eWin Rate: §f" + String.format("%.2f", data.getWinRate()) + "%");
        player.sendMessage("§6========================\n");
    }

    /**
     * Handle top command
     * @param player The player
     * @param args Command arguments
     */
    private void handleTopCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c[BattleChambers] Usage: /bc top <coins|wins|kills>");
            return;
        }

        String stat = args[1].toLowerCase();
        List<com.battlechambers.model.PlayerData> topPlayers = new ArrayList<>();

        switch (stat) {
            case "coins":
                topPlayers = plugin.getPlayerDataManager().getTopPlayersByCoins(10);
                player.sendMessage("§6===== §lTop Players by Coins §r§6=====");
                break;
            case "wins":
                topPlayers = plugin.getPlayerDataManager().getTopPlayersByWins(10);
                player.sendMessage("§6===== §lTop Players by Wins §r§6=====");
                break;
            case "kills":
                topPlayers = plugin.getPlayerDataManager().getTopPlayersByKills(10);
                player.sendMessage("§6===== §lTop Players by Kills §r§6=====");
                break;
            default:
                player.sendMessage("§c[BattleChambers] Unknown stat: " + stat);
                return;
        }

        int rank = 1;
        for (com.battlechambers.model.PlayerData data : topPlayers) {
            String value = "";
            if (stat.equals("coins")) value = String.valueOf(data.getCoins());
            else if (stat.equals("wins")) value = String.valueOf(data.getWins());
            else if (stat.equals("kills")) value = String.valueOf(data.getKills());

            player.sendMessage("§e#" + rank + " §f" + data.getUsername() + " §7- §a" + value);
            rank++;
        }
        player.sendMessage("§6========================\n");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("help");
            suggestions.add("arena");
            suggestions.add("game");
            suggestions.add("join");
            suggestions.add("leave");
            suggestions.add("stats");
            suggestions.add("top");
        } else if (args.length == 2) {
            String first = args[0].toLowerCase();
            if (first.equals("arena")) {
                suggestions.add("list");
                suggestions.add("info");
            } else if (first.equals("game")) {
                suggestions.add("list");
                suggestions.add("info");
            } else if (first.equals("top")) {
                suggestions.add("coins");
                suggestions.add("wins");
                suggestions.add("kills");
            }
        }

        return suggestions;
    }
}
