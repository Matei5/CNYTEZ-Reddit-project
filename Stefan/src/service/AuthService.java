package service;

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
            return false;
        }

        if (userRepository.findByEmail(email) != null) {
            return false;
        }

        User user = new User(nextUserId, name, username, email, password);
        nextUserId++;

        userRepository.save(user);

        return true;
    }

    public boolean login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return false;
        }

        if (!user.checkPassword(password)) {
            return false;
        }

        currentUser = user;
        return true;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}