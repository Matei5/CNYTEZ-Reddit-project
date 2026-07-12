package repository;

import model.User;
import java.util.List;

public interface UserRepository {
    void save(User user);

    User findById(int id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    void deleteById(int id);
}
