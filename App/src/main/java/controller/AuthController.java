package controller;

import exceptions.service.ServiceException;
import log.LogManager;
import service.AuthService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

public class AuthController {
    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.consoleReader = ConsoleReader.getInstance();
        this.consolePrinter = ConsolePrinter.getInstance();
        this.authService = authService;
    }

    public void register() {
        consolePrinter.printHeader("Register");

        String name = consoleReader.readText("Name: ");
        String username = consoleReader.readText("Username: ");
        String email = consoleReader.readText("Email: ");
        String password = consoleReader.readText("Password: ");

        boolean success = false;

        try {
            success = authService.register(name, username, email, password);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Account created.");
        } else {
            consolePrinter.printMessage("Could not create account. Username or email already exists.");
        }
    }

    public void login() {
        consolePrinter.printHeader("Login");

        String username = consoleReader.readText("Username: ");
        String password = consoleReader.readText("Password: ");

        boolean success = false;

        try {
            success = authService.login(username, password);
        } catch (Exception e) {
        LogManager.getInstance().log(e.getMessage());
    }

        if (success) {
            consolePrinter.printLoggedInMessage();
        } else {
            consolePrinter.printMessage("Wrong username or password.");
        }
    }
}
