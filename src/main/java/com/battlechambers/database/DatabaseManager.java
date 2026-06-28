package com.battlechambers.database;

import com.battlechambers.config.ConfigManager;
import com.battlechambers.utils.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connections using HikariCP connection pooling
 * Supports both MySQL and SQLite databases
 * 
 * Features:
 * - Connection pooling for performance
 * - Automatic connection management
 * - Query execution
 * - Table initialization
 * 
 * @author BattleChambers Team
 * @version 1.0.0
 */
public class DatabaseManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private HikariDataSource dataSource;
    private String databaseType;

    /**
     * Constructor for DatabaseManager
     * @param plugin The main plugin instance
     * @param configManager The configuration manager
     */
    public DatabaseManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    /**
     * Connect to the database
     * @throws SQLException If connection fails
     */
    public void connect() throws SQLException {
        try {
            String type = (String) configManager.get("database.yml", "database.type", "sqlite");
            this.databaseType = type.toLowerCase();

            if ("mysql".equals(this.databaseType)) {
                connectToMySQL();
            } else if ("sqlite".equals(this.databaseType)) {
                connectToSQLite();
            } else {
                throw new SQLException("Unknown database type: " + type);
            }

            // Initialize database tables
            initializeTables();
            Logger.info("Database connected and initialized successfully!");

        } catch (Exception e) {
            Logger.error("Failed to connect to database: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    /**
     * Connect to MySQL database
     * @throws SQLException If connection fails
     */
    private void connectToMySQL() throws SQLException {
        try {
            String host = (String) configManager.get("database.yml", "database.mysql.host", "localhost");
            int port = (Integer) configManager.get("database.yml", "database.mysql.port", 3306);
            String username = (String) configManager.get("database.yml", "database.mysql.username", "root");
            String password = (String) configManager.get("database.yml", "database.mysql.password", "password");
            String database = (String) configManager.get("database.yml", "database.mysql.database", "battlechambers");
            int poolSize = (Integer) configManager.get("database.yml", "database.connection-pool.size", 10);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(poolSize);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setAutoCommit(true);

            this.dataSource = new HikariDataSource(config);
            Logger.info("Connected to MySQL database: " + host + ":" + port);

        } catch (Exception e) {
            Logger.error("Failed to connect to MySQL: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    /**
     * Connect to SQLite database
     * @throws SQLException If connection fails
     */
    private void connectToSQLite() throws SQLException {
        try {
            String fileName = (String) configManager.get("database.yml", "database.file", "data.db");
            String dbPath = plugin.getDataFolder().getAbsolutePath() + "/" + fileName;

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:sqlite:" + dbPath);
            config.setMaximumPoolSize(5);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(30000);
            config.setAutoCommit(true);

            this.dataSource = new HikariDataSource(config);
            Logger.info("Connected to SQLite database: " + dbPath);

        } catch (Exception e) {
            Logger.error("Failed to connect to SQLite: " + e.getMessage());
            throw new SQLException(e);
        }
    }

    /**
     * Initialize database tables
     */
    private void initializeTables() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Players table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS players (" +
                "uuid VARCHAR(36) PRIMARY KEY," +
                "username VARCHAR(16) NOT NULL," +
                "level INT DEFAULT 1," +
                "xp BIGINT DEFAULT 0," +
                "coins BIGINT DEFAULT 0," +
                "wins INT DEFAULT 0," +
                "kills INT DEFAULT 0," +
                "deaths INT DEFAULT 0," +
                "games_played INT DEFAULT 0," +
                "playtime_seconds BIGINT DEFAULT 0," +
                "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "last_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // Games table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS game_stats (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "uuid VARCHAR(36) NOT NULL," +
                "game_name VARCHAR(50) NOT NULL," +
                "position INT NOT NULL," +
                "coins_earned INT DEFAULT 0," +
                "xp_earned INT DEFAULT 0," +
                "kills INT DEFAULT 0," +
                "deaths INT DEFAULT 0," +
                "played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (uuid) REFERENCES players(uuid)" +
                ")"
            );

            // Quests table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS quests (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "uuid VARCHAR(36) NOT NULL," +
                "quest_type VARCHAR(20) NOT NULL," +
                "quest_id VARCHAR(50) NOT NULL," +
                "progress INT DEFAULT 0," +
                "completed BOOLEAN DEFAULT FALSE," +
                "completed_at TIMESTAMP," +
                "FOREIGN KEY (uuid) REFERENCES players(uuid)" +
                ")"
            );

            // Party table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS parties (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "leader_uuid VARCHAR(36) NOT NULL," +
                "party_name VARCHAR(50) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // Party members table
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS party_members (" +
                "party_id INT NOT NULL," +
                "uuid VARCHAR(36) NOT NULL," +
                "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (party_id, uuid)," +
                "FOREIGN KEY (party_id) REFERENCES parties(id)" +
                ")"
            );

            Logger.info("Database tables initialized successfully!");

        } catch (SQLException e) {
            Logger.error("Failed to initialize database tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get a database connection from the pool
     * @return A Connection object
     * @throws SQLException If unable to get connection
     */
    public Connection getConnection() throws SQLException {
        if (this.dataSource == null) {
            throw new SQLException("Database connection pool is not initialized");
        }
        return this.dataSource.getConnection();
    }

    /**
     * Execute an async database query
     * @param query The SQL query to execute
     * @param callback The callback to handle results
     */
    public void executeAsync(String query, DatabaseCallback callback) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                if (callback != null) {
                    callback.onResult(stmt.executeQuery(query));
                }
            } catch (SQLException e) {
                Logger.error("Database query error: " + e.getMessage());
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    /**
     * Execute an async database update
     * @param query The SQL update query
     * @param callback The callback to handle results
     */
    public void executeUpdateAsync(String query, DatabaseCallback callback) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
                int affectedRows = stmt.executeUpdate(query);
                if (callback != null) {
                    callback.onUpdate(affectedRows);
                }
            } catch (SQLException e) {
                Logger.error("Database update error: " + e.getMessage());
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    /**
     * Disconnect from the database
     */
    public void disconnect() {
        try {
            if (this.dataSource != null && !this.dataSource.isClosed()) {
                this.dataSource.close();
                Logger.info("Database connection closed");
            }
        } catch (Exception e) {
            Logger.error("Error disconnecting from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if database is connected
     * @return True if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return this.dataSource != null && !this.dataSource.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the database type (MySQL or SQLite)
     * @return The database type
     */
    public String getDatabaseType() {
        return this.databaseType;
    }
}
