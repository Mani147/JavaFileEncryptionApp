package com.example.FileEncryptionApp;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FileLogger {

    public static void log(String fileName, String filePath, String status) {
        String sql = "INSERT INTO file_logs(file_name, file_path, status) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseUtil.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fileName);
            pstmt.setString(2, filePath);
            pstmt.setString(3, status);
            pstmt.executeUpdate();
            System.out.println("Log inserted: " + fileName + " - " + status);
        } catch (Exception e) {
            System.out.println("Logging Error: " + e.getMessage());
        }
    }
}