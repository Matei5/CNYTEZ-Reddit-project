package repository;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;

    private List<User> users;

    private UserRepository() {
        users = new ArrayList<>();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    public void save(User user) {
        users.add(user);
    }

    public User findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    public void deleteById(int id) {
        users.removeIf(user -> user.getId() == id);
    }
}