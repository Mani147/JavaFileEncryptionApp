package com.example.FileEncryptionApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogViewer {

    public static void showLogs() {
        Stage stage = new Stage();
        TableView<LogEntry> table = new TableView<>();
        ObservableList<LogEntry> data = FXCollections.observableArrayList();

        // Columns
        TableColumn<LogEntry, String> fileNameCol = new TableColumn<>("File Name");
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        TableColumn<LogEntry, String> filePathCol = new TableColumn<>("File Path");
        filePathCol.setCellValueFactory(new PropertyValueFactory<>("filePath"));

        TableColumn<LogEntry, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<LogEntry, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        table.getColumns().addAll(fileNameCol, filePathCol, statusCol, timestampCol);

        // Fetch data from DB
        try (Connection conn = DatabaseUtil.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM file_logs")) {

            while (rs.next()) {
                data.add(new LogEntry(
                        rs.getString("file_name"),
                        rs.getString("file_path"),
                        rs.getString("status"),
                        rs.getString("timestamp")
                ));
            }
        } catch (Exception e) {
            System.out.println("Error loading logs: " + e.getMessage());
        }

        table.setItems(data);

        VBox root = new VBox(table);
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.setTitle("Operation Logs");
        stage.show();
    }
}
