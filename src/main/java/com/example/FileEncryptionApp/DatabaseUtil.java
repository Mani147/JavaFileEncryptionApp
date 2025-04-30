package com.example.FileEncryptionApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseUtil {
    private static final String URL = "jdbc:sqlite:file_logs.db";  // SQLite file

    // Establish connection to SQLite
    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    // Create table if not exists
    public static void initializeDatabase() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS file_logs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "file_name TEXT NOT NULL," +
                    "file_path TEXT NOT NULL," +
                    "status TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            System.out.println("DB Init Error: " + e.getMessage());
        }
    }
}
