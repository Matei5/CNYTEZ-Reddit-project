import log.ConsoleLogger;
import log.FileLogger;
import log.LogManager;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        LogManager.getInstance().addLogger(ConsoleLogger.getInstance());
        LogManager.getInstance().addLogger(FileLogger.getInstance());

        Menu menu = new Menu();
        menu.start();
    }
}