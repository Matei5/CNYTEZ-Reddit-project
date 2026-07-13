package service;

import exceptions.repository.RepositoryException;
import log.LogManager;
import model.User;

import repository.UserRepository;

public class AuthService {
    private final UserRepository userRepository;
    private int nextUserId;
    private User currentUser;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.nextUserId = 1;
        this.currentUser = null;
    }
    public boolean register(String name, String username, String email, String password) throws RepositoryException {
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

    public boolean login(String username, String password) throws RepositoryException {
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
        if (currentUser == null) {
            LogManager.getInstance().log("Logout failed! No user was logged in");
            return;
        }

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
