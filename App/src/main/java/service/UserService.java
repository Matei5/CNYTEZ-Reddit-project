package service;

import model.User;
import repository.UserRepository;

public class UserService {
    private static UserService instance;

    private UserRepository userRepository;

    private UserService() {
        userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }

        return instance;
    }

    public boolean deleteUser(int userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return false;
        }

        userRepository.deleteById(userId);
        return true;
    }
}