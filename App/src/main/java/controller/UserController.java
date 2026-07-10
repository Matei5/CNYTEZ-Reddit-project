package controller;

import model.User;
import repository.UserRepository;
import service.AuthService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

public class UserController {

    private static UserController instance;

    private final ConsolePrinter consolePrinter;
    private final UserRepository userRepository;
    private final AuthService authService;

    private UserController() {
        this.consolePrinter = ConsolePrinter.getInstance();
        this.userRepository = UserRepository.getInstance();
        this.authService = AuthService.getInstance();
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public void printUserProfile() {
        consolePrinter.printHeader("My Profile");
        User user = authService.getLoggedInUser();

        if (user == null) {
            consolePrinter.printMessage("No user logged in.");
            return;
        }

        int karma = userRepository.getUserKarma(user.getId());

        consolePrinter.printUserProfile(user, karma);
    }
}
