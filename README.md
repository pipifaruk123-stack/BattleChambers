# BattleChambers 🎮

> A comprehensive mini-game server plugin for Minecraft built with Bukkit/Spigot

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/minecraft-1.20-green)
![Java](https://img.shields.io/badge/java-11+-orange)
![License](https://img.shields.io/badge/license-MIT-brightgreen)

---

## 📋 Overview

BattleChambers is a feature-rich Minecraft plugin that provides a complete mini-game experience. The plugin includes **10+ unique mini-games**, arena management, player progression systems, and comprehensive leaderboards.

## 🎯 Features

### Mini-Games
- **Bow Battle** - Fight with bows and arrows
- **Sword Fight** - Classic melee combat with diamond swords
- **TNT Run** - Race across disappearing TNT blocks
- **Block Party** - Stand on the correct disappearing blocks
- **Parkour Challenge** - Complete challenging parkour courses
- **Sumo Arena** - Push opponents off platforms
- **Lava Escape** - Survive rising lava
- **Snowball Fight** - Battle with snowballs
- **Fish Challenge** - Compete to catch the most fish
- **Hot Potato** - Pass the potato before it explodes

### Core Features
- 🏛️ **Arena Management** - Multiple arenas with customizable settings
- 👥 **Player Progression** - Level up, earn XP, and collect coins
- 📊 **Statistics Tracking** - Track kills, deaths, wins, and more
- 🏆 **Leaderboards** - Global and category-specific rankings
- ⚙️ **Fully Configurable** - Customize games, arenas, rewards, and more
- 💾 **Data Persistence** - SQLite/MySQL support for player data
- 🎮 **Event System** - Comprehensive event listeners
- 💬 **Command System** - Full command support with tab completion

## 📦 Installation

### Requirements
- Minecraft Server 1.20+
- Java 11+
- Bukkit/Spigot/Paper server software

### Steps

1. **Download the plugin JAR**
   ```bash
   # Build from source
   mvn clean package
   ```

2. **Place in plugins folder**
   ```bash
   cp target/BattleChambers-1.0.0.jar /path/to/server/plugins/
   ```

3. **Start your server**
   ```bash
   # The plugin will generate configuration files automatically
   ```

4. **Configure (optional)**
   - Edit `plugins/BattleChambers/config.yml`
   - Customize arenas in `plugins/BattleChambers/arenas.yml`
   - Adjust games in `plugins/BattleChambers/games.yml`

## 🎮 Usage

### Commands

```bash
# View help
/bc help

# List arenas
/bc arena list

# List available games
/bc game list

# Join a game
/bc join <game_name>

# Leave current game
/bc leave

# View your statistics
/bc stats

# View leaderboards
/bc top <coins|wins|kills>
```

### Permissions

```yaml
battlechambers.use          # Use BattleChambers commands (default: true)
battlechambers.admin        # Admin commands (default: op)
battlechambers.create.arena # Create arenas (default: op)
battlechambers.create.game  # Create games (default: op)
```

## 🔧 Configuration

### config.yml
```yaml
plugin:
  name: BattleChambers
  version: 1.0.0

database:
  type: sqlite  # or mysql
  
players:
  auto_save_interval: 300
  xp_multiplier: 1.0
  coin_multiplier: 1.0
```

### arenas.yml
```yaml
arenas:
  arena_1:
    name: "Battle Arena 1"
    world: "battlechambers_world"
    max_players: 24
    min_players: 2
```

### games.yml
```yaml
games:
  bow-battle:
    name: "Bow Battle"
    enabled: true
    duration: 120
    rewards:
      win: 50
      kill: 10
```

## 📁 Project Structure

```
BattleChambers/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/battlechambers/
│   │   │       ├── BattleChambers.java (Main plugin class)
│   │   │       ├── games/ (Mini-game implementations)
│   │   │       ├── command/ (Command handlers)
│   │   │       ├── listener/ (Event listeners)
│   │   │       ├── manager/ (Manager classes)
│   │   │       ├── model/ (Data models)
│   │   │       ├── config/ (Configuration)
│   │   │       └── utils/ (Utilities)
│   │   └── resources/
│   │       ├── plugin.yml
│   │       ├── config.yml
│   │       ├── arenas.yml
│   │       └── games.yml
│   └── test/ (Unit tests)
├── pom.xml (Maven configuration)
└── README.md (This file)
```

## 🏗️ Architecture

### Core Components

1. **BattleChambers** - Main plugin class managing initialization and lifecycle
2. **GameManager** - Manages game instances and orchestration
3. **ArenaManager** - Manages arena creation and configuration
4. **PlayerDataManager** - Handles player statistics and persistence
5. **ConfigManager** - Manages configuration loading and saving

### Game System

Each mini-game extends `AbstractMiniGame` and implements:
- `initialize()` - Setup game state
- `start()` - Begin game
- `tick()` - Per-tick game logic
- `end()` - Cleanup
- `onPlayerDeath()` - Death handling
- `getWinners()` - Determine winners

## 🧪 Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

## 📊 Database Schema

The plugin uses SQLite by default with the following tables:

```sql
player_data
  - uuid (PRIMARY KEY)
  - username
  - level
  - xp
  - coins
  - wins
  - kills
  - deaths

game_stats
  - id (PRIMARY KEY)
  - player_uuid
  - game_name
  - date
  - kills
  - deaths
  - won

arena_data
  - id (PRIMARY KEY)
  - name
  - world
  - max_players
  - enabled
```

## 🚀 Performance

- **Asynchronous Data Loading** - Player data loads without blocking
- **Efficient Event Handling** - Optimized event listeners
- **Scheduled Auto-Save** - Data saved every 5 minutes
- **Configurable Tick Rate** - Adjust game tick frequency

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **BattleChambers Team** - Initial work
- **pipifaruk123-stack** - Project maintainer

## 🙏 Acknowledgments

- Bukkit/Spigot community for excellent server software
- Minecraft plugin developers for inspiration
- All contributors and testers

## 📞 Support

For issues, questions, or suggestions:

- 🐛 [Report Issues](https://github.com/pipifaruk123-stack/BattleChambers/issues)
- 💬 [Discussions](https://github.com/pipifaruk123-stack/BattleChambers/discussions)
- 📧 Contact: pipifaruk123@gmail.com

## 🗺️ Roadmap

- [ ] Custom game creation system
- [ ] Party and team system
- [ ] Achievement system
- [ ] Cosmetics and skins
- [ ] Cross-server play
- [ ] Mobile companion app
- [ ] Advanced statistics dashboard

---

**BattleChambers** - Making Minecraft mini-games fun and competitive! 🎮✨
