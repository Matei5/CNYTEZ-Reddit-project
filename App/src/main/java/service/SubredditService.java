package service;

import log.LogManager;
import model.Subreddit;
import model.User;
import repository.SubredditRepository;
import repository.UserRepository;

import java.time.LocalDateTime;

public class SubredditService {
    private int nextSubredditId;

    private final SubredditRepository subRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public SubredditService(SubredditRepository subRepository, UserRepository userRepository, AuthService authService) {
        this.subRepository = subRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.nextSubredditId = 1;
    }

    public boolean createSubreddit(String name, String photo, String banner) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Create subreddit failed! No user was logged in");

            return false;
        }

        if (subRepository.getSubredditByName(name) != null) {
            LogManager.getInstance().log(
                "Create subreddit failed! User with id " + loggedUser.getId() +
                " tried to create subreddit with name " + name + " that already exists"
            );

            return false;
        }

        int ownerId = loggedUser.getId();
        LocalDateTime creationDate = LocalDateTime.now();

        Subreddit sub = new Subreddit(nextSubredditId,name, photo, banner, ownerId, creationDate);
        nextSubredditId++;

        sub.addFollower(loggedUser.getId());

        subRepository.addSubreddit(sub);
        LogManager.getInstance().log(
            "Create subreddit success! User with id " + loggedUser.getId() + " created subreddit with name " + name
        );

        return true;
    }

    public boolean changeOwner(String username, int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Change owner of subreddit failed! No user was logged in");

            return false;
        }

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null) {
            LogManager.getInstance().log(
                "Change owner of subreddit failed! User with id " + loggedUser.getId() +
                " tried to change owner of subreddit with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (loggedUser.getId() != sub.getOwnerId()) {
            LogManager.getInstance().log(
                "Change owner of subreddit failed! User with id " + loggedUser.getId() +
                " is not the owner of subreddit with id " + id
            );

            return false;
        }

        User newOwner = userRepository.findByUsername(username);
        if (newOwner == null) {
            LogManager.getInstance().log(
                "Change owner of subreddit failed! User with id " + loggedUser.getId() +
                " tried to change owner of subreddit with id " + id + " to a user with username " +
                username + " that doesn't exist"
            );

            return false;
        }

        sub.setOwnerId(newOwner.getId());
        sub.addFollower(newOwner.getId());
        subRepository.update(sub);

        LogManager.getInstance().log(
            "Change owner of subreddit success! User with id " + loggedUser.getId() +
            " changed the owner of the subreddit with id " + id + " to a user with username " + username
        );

        return true;
    }

    public boolean joinSubreddit(int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Join subreddit failed! No user was logged in");

            return false;
        }

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null) {
            LogManager.getInstance().log(
                "Join subreddit failed! User with id " + loggedUser.getId() +
                " tried to join subreddit with id " + id + " that doesn't exist"
            );

            return false;
        }

        boolean hasJoined = sub.addFollower(loggedUser.getId());
        if (!hasJoined) {
            LogManager.getInstance().log(
                "Join subreddit failed! User with id " + loggedUser.getId() + " already joined subreddit with id " + id
            );

            return false;
        } else subRepository.update(sub);

        LogManager.getInstance().log(
            "Join subreddit success! User with id " + loggedUser.getId() + " joined subreddit with id" + id
        );

        return true;
    }

    public boolean leaveSubreddit(int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Leave subreddit failed! No user was logged in");

            return false;
        }

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null) {
            LogManager.getInstance().log(
                "Join subreddit failed! User with id " + loggedUser.getId() +
                " tried to leave subreddit with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (loggedUser.getId() == sub.getOwnerId()) { // Owner needs to change subreddit ownership before leaving
            LogManager.getInstance().log(
                "Leave subreddit failed! The user with id " + loggedUser.getId() +
                " tried to leave subreddit with id " + id + " as owner of the subreddit"
            );

            return false;
        }

        boolean hasLeft = sub.removeFollower(loggedUser.getId());
        if (!hasLeft) {
            LogManager.getInstance().log(
                "Leave subreddit failed! The user with id " + loggedUser.getId() +
                " hasn't joined subreddit with id " + id
            );

            return false;
        } else  subRepository.update(sub);

        LogManager.getInstance().log(
            "Leave subreddit success! The user with id " + loggedUser.getId() + " left subreddit with id " + id
        );

        return true;
    }

    public boolean changePhoto(String photo, int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Change photo of subreddit failed! No user was logged in");

            return false;
        }

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null) {
            LogManager.getInstance().log(
                "Change photo of subreddit failed! User with id " + loggedUser.getId() +
                " tried to change photo of subreddit with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (loggedUser.getId() != sub.getOwnerId()) {
            LogManager.getInstance().log(
                "Change photo of subreddit failed! User with id " + loggedUser.getId() +
                " is not the owner of subreddit with id " + id
            );

            return false;
        }

        sub.setPhoto(photo);
        subRepository.update(sub);

        LogManager.getInstance().log(
            "Change photo of subreddit success! User with id " + loggedUser.getId() +
            " changed photo of subreddit with id " + id
        );

        return true;
    }

    public boolean changeBanner(String banner, int id) {
        User loggedUser = authService.getLoggedInUser();
        if (loggedUser == null) {
            LogManager.getInstance().log("Change banner of subreddit failed! No user was logged in");

            return false;
        }

        Subreddit sub = subRepository.getSubredditById(id);
        if (sub == null) {
            LogManager.getInstance().log(
                "Change banner of subreddit failed! User with id " + loggedUser.getId() +
                " tried to change banner of subreddit with id " + id + " that doesn't exist"
            );

            return false;
        }

        if (loggedUser.getId() != sub.getOwnerId()) {
            LogManager.getInstance().log(
                "Change banner of subreddit failed! User with id " + loggedUser.getId() +
                " is not the owner of subreddit with id " + id
            );

            return false;
        }

        sub.setBanner(banner);
        subRepository.update(sub);

        LogManager.getInstance().log(
            "Change banner of subreddit success! User with id " + loggedUser.getId() +
            " changed banner of subreddit with id " + id
        );

        return true;
    }
}
