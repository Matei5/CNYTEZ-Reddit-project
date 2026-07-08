package ui;

import java.util.Scanner;

public class ConsoleReader {
    private final Scanner scanner;

    public ConsoleReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readText(String message){
       System.out.println(message);
       return scanner.nextLine();
    }

    public int readInt(String message){
        while (true) {
            System.out.println(message);
            String input = scanner.nextLine();

            try {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println("Please enter an a valid number.");
            }
        }
    }
}
