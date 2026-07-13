package controller;

import exceptions.repository.RepositoryException;
import log.LogManager;
import model.Subreddit;
import model.User;
import repository.SubredditRepository;
import repository.UserRepository;
import service.SubredditService;
import ui.ConsolePrinter;
import ui.ConsoleReader;

import java.util.ArrayList;
import java.util.List;

public class SubredditController {
    private final ConsoleReader consoleReader;
    private final ConsolePrinter consolePrinter;
    private final SubredditService subredditService;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;

    public SubredditController(
            SubredditService subredditService,
            SubredditRepository subredditRepository,
            UserRepository userRepository
    ) {
        this.consoleReader = ConsoleReader.getInstance();
        this.consolePrinter = ConsolePrinter.getInstance();
        this.subredditService = subredditService;
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
    }

    public void listSubreddits() {
        consolePrinter.printHeader("Subreddits");

        List<Subreddit> subreddits = new ArrayList<>();

        try {
            subreddits = subredditRepository.getAllSubreddits();
        } catch (RepositoryException e) {
            LogManager.getInstance().log(e.getMessage());
        }

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

        boolean success = false;

        try {
            success = subredditService.createSubreddit(name, photo, banner);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Subreddit created.");
        } else {
            consolePrinter.printMessage("Could not create subreddit. Make sure the name is unique.");
        }
    }

    public void joinSubreddit() {
        consolePrinter.printHeader("Join subreddit");

        int subredditId = consoleReader.readInt("Subreddit id: ");

        boolean success = false;

        try {
            success = subredditService.joinSubreddit(subredditId);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Joined subreddit.");
        } else {
            consolePrinter.printMessage("Could not join subreddit. It may not exist or you may already be a follower.");
        }
    }

    public void leaveSubreddit() {
        consolePrinter.printHeader("Leave subreddit");

        int subredditId = consoleReader.readInt("Subreddit id: ");

        boolean success = false;

        try {
            success = subredditService.leaveSubreddit(subredditId);
        } catch (Exception e) {
            LogManager.getInstance().log(e.getMessage());
        }

        if (success) {
            consolePrinter.printMessage("Left subreddit.");
        } else {
            consolePrinter.printMessage("Could not leave subreddit. Owners cannot leave their own subreddit.");
        }
    }

    private void printSubreddit(Subreddit subreddit) {
        User owner = null;

        try {
            owner = userRepository.findById(subreddit.getOwnerId());
        } catch (RepositoryException e) {
            LogManager.getInstance().log(e.getMessage());
        }

        String ownerUsername = owner == null ? "unknown" : owner.getUsername();
        consolePrinter.printSubreddit(subreddit, ownerUsername);
    }
}
