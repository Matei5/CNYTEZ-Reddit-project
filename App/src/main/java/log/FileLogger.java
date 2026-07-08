package log;

import java.time.LocalDateTime;

public class FileLogger {
    private static FileLogger instance;
    private final LogWriter writer;

    public static FileLogger getInstance() {
        if (instance == null) instance = new FileLogger();
        return instance;
    }

    private FileLogger() {
        this.writer = new LogWriter();
    }

    public void log(String message) {
        LocalDateTime timeStamp = LocalDateTime.now();
        writer.logToFile("[" + timeStamp + "]" + message);
    }
}