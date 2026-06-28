# BattleChambers

**Professional Mini-Games Plugin for Minecraft Paper 1.21.8**

> Inspired by CraftRise Herobrine Chamber • Fully Custom • Production Grade • Optimized

## Features

✅ **50+ Unique Mini-Games**
- Bow Battle, Sword Fight, TNT Run, Block Party, Parkour, and more
- Each game fully customizable and extensible

✅ **Advanced Game Systems**
- Point System (1st: 10pts, 2nd: 8pts, etc.)
- Coin Economy (Kills, Assists, Quests)
- 100-Level XP System
- Daily & Weekly Quests
- Party System

✅ **Professional Architecture**
- Clean Code & SOLID Principles
- Dependency Injection
- Async Operations
- Database Abstraction (MySQL & SQLite)

✅ **Integration Support**
- PlaceholderAPI
- LuckPerms
- Vault (Economy)
- Citizens (NPC)

✅ **Modern UI/UX**
- Inventory GUI System
- Dynamic Scoreboard
- ActionBar Updates
- BossBar Animations

## Requirements

- **Java**: 21+
- **Minecraft Server**: Paper 1.21.8+
- **Database**: MySQL 8.0+ or SQLite

## Installation

1. Download the latest JAR from Releases
2. Place in `plugins/` folder
3. Restart server
4. Configure `/plugins/BattleChambers/config.yml`
5. Use `/bc setup` for initial setup

## Commands

```
/bc help              - Show help message
/bc reload            - Reload configuration
/bc start             - Start a game
/bc stop              - Stop current game
/bc forcestart        - Force start with minimum players
/bc setlobby          - Set lobby location
/bc createarena       - Create new arena
/bc deletearena       - Delete arena
/bc setspawn          - Set game spawn
/bc setspec           - Set spectator spawn
/bc setnpc            - Set NPC location
/bc coin <player> <amount>  - Give coins
/bc xp <player> <amount>    - Give XP
/bc level <player> <level>  - Set player level
/bc stats <player>    - Show player stats
```

## Permissions

```
battle.admin         - All commands
battle.reload        - Reload configuration
battle.start         - Start games
battle.stop          - Stop games
battle.setup         - Setup commands
battle.stats         - View statistics
```

## Configuration

All settings are in `/plugins/BattleChambers/`:

- `config.yml` - Main settings
- `arenas.yml` - Arena configurations
- `games.yml` - Game settings
- `messages.yml` - Language/messages
- `menus.yml` - GUI menus
- `sounds.yml` - Sound effects
- `scoreboard.yml` - Scoreboard design
- `database.yml` - Database connection

## Architecture

```
src/main/java/com/battlechambers/
├── api/              - Public API
├── arena/            - Arena management
├── commands/         - Command system
├── config/           - Configuration loading
├── database/         - Database layer
├── events/           - Event handlers
├── game/             - Core game logic
├── games/            - Mini-game implementations (50+)
├── gui/              - Inventory GUI system
├── listeners/        - Bukkit event listeners
├── manager/          - Manager services
├── model/            - Data models
├── npc/              - NPC integration
├── player/           - Player management
├── reward/           - Reward system
├── scoreboard/       - Scoreboard management
├── scheduler/        - Task scheduling
├── sound/            - Sound management
├── storage/          - Data storage
├── tasks/            - Background tasks
├── utils/            - Utility classes
└── language/         - Localization
```

## Mini-Games (50+)

1. Bow Battle
2. Sword Fight
3. Crossbow Fight
4. Sumo
5. TNT Run
6. Block Party
7. Lava Escape
8. Water Escape
9. Parkour
10. Elytra Race
... and 40+ more

## Development

### Adding a New Mini-Game

1. Create a class extending `AbstractMiniGame`
2. Implement required methods
3. Register in game manager
4. Configure in `games.yml`

```java
public class MyGame extends AbstractMiniGame {
    @Override
    public void start() { /* ... */ }
    
    @Override
    public void end() { /* ... */ }
}
```

## Performance Optimization

- ✅ Async database operations
- ✅ Object pooling for entities
- ✅ Packet optimization
- ✅ Efficient chunk loading
- ✅ Memory leak prevention
- ✅ Cache system

## Support

For issues and feature requests, visit: https://github.com/pipifaruk123-stack/BattleChambers/issues

## License

This project is proprietary and closed-source.

---

**Made with ❤️ for Minecraft Servers**