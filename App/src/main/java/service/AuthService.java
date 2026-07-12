package service;

import log.LogManager;
import model.User;
import repository.UserRepository;

public class AuthService {
    private static AuthService instance;

    private UserRepository userRepository;
    private int nextUserId;
    private User currentUser;

    private AuthService() {
        userRepository = UserRepository.getInstance();
        nextUserId = 1;
        currentUser = null;
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }

        return instance;
    }

    public boolean register(String name, String username, String email, String password) {
        if (userRepository.findByUsername(username) != null) {
            LogManager.getInstance().log("Register failed! Username " + username + " already exists");

            return false;
        }

        if (userRepository.findByEmail(email) != null) {
            LogManager.getInstance().log("Register failed! Email " + email + " already exists");

            return false;
        }

        User user = new User(nextUserId, name, username, email, password);
        nextUserId++;

        userRepository.save(user);
        LogManager.getInstance().log("Register success! User with id " + user.getId() + " registered");

        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            LogManager.getInstance().log("Login failed! Username " + username + " doesn't exist");

            return false;
        }

        if (!user.checkPassword(password)) {
            LogManager.getInstance().log("Login failed! Password incorrect for user with id " + user.getId());

            return false;
        }

        currentUser = user;
        LogManager.getInstance().log("Login success! User with id " + user.getId() + " logged in");

        return true;
    }

    public void logout() {
        LogManager.getInstance().log("Logout success! User with id " + currentUser.getId() + " logged out");

        currentUser = null;
    }

    public User getLoggedInUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}