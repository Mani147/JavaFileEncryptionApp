package com.example.FileEncryptionApp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class App extends Application {
    private static final String SECRET_KEY = "MySuperSecretKey";
    private Stage primaryStage;

    @Override
    public void start(@org.jetbrains.annotations.NotNull Stage primaryStage) {

        DatabaseUtil.initializeDatabase();

        this.primaryStage = primaryStage;
        Button viewLogsButton = new Button("View Logs");
        viewLogsButton.setOnAction(e -> LogViewer.showLogs());

        Label instructionLabel = new Label("Drag and Drop a file to Encrypt/Decrypt");
        Text fileText = new Text("No file selected");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(-1);  // Indeterminate mode
        progressBar.setVisible(false);

        VBox root = new VBox(10, instructionLabel, fileText, progressBar, viewLogsButton);
        root.setStyle("-fx-padding: 20px; -fx-alignment: center; -fx-border-color: #333;");

        root.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) event.acceptTransferModes(TransferMode.COPY);
            event.consume();
        });

        root.setOnDragDropped((DragEvent event) -> {
            var db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                Platform.runLater(() -> {
                    fileText.setText("Processing: " + file.getName());
                    progressBar.setVisible(true);
                });

                new Thread(() -> {
                    try {
                        if (file.getName().endsWith(".enc")) {
                            decryptFile(file);
                            fileText.setText("Decrypted: " + file.getName().replace(".enc", ""));
                            FileLogger.log(file.getName(), file.getAbsolutePath(), "Decrypted");
                        } else {
                            encryptFile(file);
                            fileText.setText("Encrypted: " + file.getName() + ".enc");
                            FileLogger.log(file.getName(), file.getAbsolutePath(), "Encrypted");
                        }
                    } catch (Exception e) {
                        fileText.setText("Error: " + e.getMessage());
                        FileLogger.log(file.getName(), file.getAbsolutePath(), "Error: " + e.getMessage());
                    } finally {
                        progressBar.setVisible(false);
                    }
                }).start();

            }
            event.setDropCompleted(true);
            event.consume();
        });

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("File Encryption & Decryption");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void encryptFile(File file) throws Exception {
        byte[] fileData = Files.readAllBytes(file.toPath());
        byte[] encryptedData = AESUtil.encrypt(fileData, SECRET_KEY);
        Path encryptedPath = Path.of(file.getPath() + ".enc");

        if (Files.exists(encryptedPath)) {
            throw new Exception("Encrypted file already exists!");
        }

        Files.write(encryptedPath, encryptedData, StandardOpenOption.CREATE_NEW);
    }

    private void decryptFile(File file) throws Exception {
        byte[] fileData = Files.readAllBytes(file.toPath());
        byte[] decryptedData = AESUtil.decrypt(fileData, SECRET_KEY);
        Path decryptedPath = Path.of(file.getPath().replace(".enc", ""));

        if (Files.exists(decryptedPath)) {
            throw new Exception("Decrypted file already exists!");
        }

        Files.write(decryptedPath, decryptedData, StandardOpenOption.CREATE_NEW);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
