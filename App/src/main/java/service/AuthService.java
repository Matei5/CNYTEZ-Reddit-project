package service;

import exceptions.repository.RepositoryException;
import exceptions.service.ServiceException;
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
    public boolean register(String name, String username, String email, String password) throws ServiceException, RepositoryException {
        if (userRepository.findByUsername(username) != null) {
            throw new ServiceException("Register failed! Username " + username + " already exists");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new ServiceException("Register failed! Email " + email + " already exists");
        }

        User user = new User(nextUserId, name, username, email, password);
        nextUserId++;

        userRepository.save(user);
        LogManager.getInstance().log("Register success! User with id " + user.getId() + " registered");

        return true;
    }

    public boolean login(String username, String password) throws ServiceException, RepositoryException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new ServiceException("Login failed! Username " + username + " doesn't exist");
        }

        if (!user.checkPassword(password)) {
            throw new ServiceException("Login failed! Password incorrect for user with id " + user.getId());
        }

        currentUser = user;
        LogManager.getInstance().log("Login success! User with id " + user.getId() + " logged in");

        return true;
    }

    public void logout() throws ServiceException {
        if (currentUser == null) {
            throw new ServiceException("Logout failed! No user was logged in");
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
