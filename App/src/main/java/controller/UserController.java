package controller;

import model.User;
import service.AuthService;
import service.UserService;
import ui.ConsolePrinter;

public class UserController {
    private final ConsolePrinter consolePrinter;
    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.consolePrinter = ConsolePrinter.getInstance();
        this.authService = authService;
        this.userService = userService;
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
