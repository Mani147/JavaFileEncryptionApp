package com.example.FileEncryptionApp;

public class LogEntry {
    private String fileName;
    private String filePath;
    private String status;
    private String timestamp;

    public LogEntry(String fileName, String filePath, String status, String timestamp) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
