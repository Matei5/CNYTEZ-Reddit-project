package log;

import java.util.ArrayList;
import java.util.List;

public class LogManager {
    private static LogManager instance;

    private List<Logger> loggers;

    private LogManager() {
        this.loggers = new ArrayList<>();
    }

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }

        return instance;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    public void removeLogger(Logger logger) {
        loggers.remove(logger);
    }

    public void log(String message) {
        for (Logger logger : loggers) {
            logger.log(message);
        }
    }
}
