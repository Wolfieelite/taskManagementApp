package org.wolfie.taskmanagerapp.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager() throws SQLException {
        Path dbPath = resolveDbPath();
        String url = "jdbc:sqlite:" + dbPath;
        this.connection = DriverManager.getConnection(url);
    }

    private Path resolveDbPath(){
        String userHome = System.getProperty("user.home");
        String osName = System.getProperty("os.name").toLowerCase();
        
        Path appDataDir;
        if (osName.contains("win")) {
            appDataDir = Path.of(System.getenv("APPDATA"), "TaskManagerApp");
        } else if (osName.contains("mac")) {
            appDataDir = Path.of(userHome, "Library", "Application Support", "TaskManagerApp");
        } else {
            appDataDir = Path.of(userHome, "Documents", "Databases", "TaskManagerApp");
        }

        try {
            Files.createDirectories(appDataDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create app data directory", e);
        }
        return appDataDir.resolve("taskmanager.db");
    }

    public Connection getConnection(){
       return connection;
    }

    public void close() throws SQLException{
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
