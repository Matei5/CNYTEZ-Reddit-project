package log;

import java.time.LocalDateTime;

public class ConsoleLogger implements Logger {
    private static ConsoleLogger instance;

    public static ConsoleLogger getInstance() {
        if (instance == null) instance = new ConsoleLogger();
        return instance;
    }

    private ConsoleLogger() {
    }

    @Override
    public void log(String message) {
        LocalDateTime timeStamp = LocalDateTime.now();
        System.out.println("[" + timeStamp + "]" + message);
    }
}
