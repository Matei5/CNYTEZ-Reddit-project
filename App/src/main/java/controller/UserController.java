package controller;

import model.User;
import service.AuthService;
import service.UserService;
import ui.ConsolePrinter;

public class UserController {

    private static UserController instance;

    private final ConsolePrinter consolePrinter;
    private final AuthService authService;
    private final UserService userService;

    private UserController() {
        this.consolePrinter = ConsolePrinter.getInstance();
        this.authService = AuthService.getInstance();
        this.userService = UserService.getInstance();
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

        int karma = userService.calculateKarma(user.getId());

        consolePrinter.printUserProfile(user, karma);
    }
}
