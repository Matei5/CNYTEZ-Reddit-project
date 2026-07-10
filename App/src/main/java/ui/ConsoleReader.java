package ui;

import java.util.Scanner;

public class ConsoleReader {

    private static ConsoleReader instance;

    private final Scanner scanner;
    private final ConsolePrinter consolePrinter;

    private ConsoleReader() {
        this.scanner = new Scanner(System.in);
        this.consolePrinter = ConsolePrinter.getInstance();
    }

    public static ConsoleReader getInstance() {
        if (instance == null) {
            instance = new ConsoleReader();
        }
        return instance;
    }

    public String readText() {
        return scanner.nextLine();
    }

    public String readText(String prompt) {
        consolePrinter.printPrompt(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        while (true) {
            consolePrinter.printPrompt(prompt);

            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException exception) {
                consolePrinter.printInvalidNumberMessage();
            }
        }
    }

    public void close() {
        scanner.close();
    }
}
