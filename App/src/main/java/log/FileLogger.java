package log;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileLogger implements Logger {
    private static FileLogger instance;

    public static FileLogger getInstance() {
        if (instance == null) instance = new FileLogger();
        return instance;
    }

    private FileLogger() {
    }

    @Override
    public void log(String message) {
        LocalDateTime timeStamp = LocalDateTime.now();
        try {
            FileWriter writer = new FileWriter("app.log", true);
            writer.write("[" + timeStamp + "]" + message + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to log message" + e.getMessage());
        }
    }
}