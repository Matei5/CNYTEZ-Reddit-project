package controller;

import model.Subreddit;
import model.User;
import repository.SubredditRepository;
import repository.UserRepository;
import service.AuthService;
import service.SubredditService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

import java.util.List;

public class SubredditController {

    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final SubredditService subredditService;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public SubredditController(
            ConsoleReader consoleReader,
            ConsolePrinter consolePrinter,
            SubredditService subredditService,
            SubredditRepository subredditRepository,
            UserRepository userRepository,
            AuthService authService
    ) {
        this.consoleReader = consoleReader;
        this.consolePrinter = consolePrinter;
        this.subredditService = subredditService;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public void listSubreddits() {
        consolePrinter.printHeader("Subreddits");

        List<Subreddit> subreddits = subredditRepository.getAllSubreddits();

        if (subreddits.isEmpty()) {
            consolePrinter.printMessage("No subreddits yet.");
            return;
        }

        for (Subreddit subreddit : subreddits) {
            printSubreddit(subreddit);
        }
    }

    public void createSubreddit() {
        consolePrinter.printHeader("Create subreddit");

        String name = consoleReader.readText("Name: ");
        String photo = consoleReader.readText("Photo path: ");
        String banner = consoleReader.readText("Banner path: ");

        boolean success = subredditService.createSubreddit(name, photo, banner);

        if (success) {
            consolePrinter.printMessage("Subreddit created.");
        } else {
            consolePrinter.printMessage("Could not create subreddit. Make sure the name is unique.");
        }
    }

    public void joinSubreddit() {
        consolePrinter.printHeader("Join subreddit");

        int subredditId = consoleReader.readInt("Subreddit id: ");

        boolean success = subredditService.joinSubreddit(subredditId);

        if (success) {
            consolePrinter.printMessage("Joined subreddit.");
        } else {
            consolePrinter.printMessage("Could not join subreddit. It may not exist or you may already be a follower.");
        }
    }

    public void leaveSubreddit() {
        consolePrinter.printHeader("Leave subreddit");

        int subredditId = consoleReader.readInt("Subreddit id: ");

        boolean success = subredditService.leaveSubreddit(subredditId);

        if (success) {
            consolePrinter.printMessage("Left subreddit.");
        } else {
            consolePrinter.printMessage("Could not leave subreddit. Owners cannot leave their own subreddit.");
        }
    }

    private void printSubreddit(Subreddit subreddit) {
        User owner = userRepository.findById(subreddit.getOwnerId());
        String ownerUsername = owner == null ? "unknown" : owner.getUsername();
        consolePrinter.printSubreddit(subreddit, ownerUsername);
    }
}
