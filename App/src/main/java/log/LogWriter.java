package log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogWriter {
    private PrintWriter writer;

    public LogWriter() {
        try {
            FileWriter fileWriter = new FileWriter("app.log", true);
            this.writer = new PrintWriter(fileWriter, true);
        } catch (IOException e) {
            System.err.println("Failed to initialize log writer" + e.getMessage());
        }
    }

    public void logToFile(String message) {
        if (writer != null)
            writer.println(message);
    }
}
