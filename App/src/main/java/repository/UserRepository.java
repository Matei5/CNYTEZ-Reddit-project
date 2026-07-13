package repository;

import exceptions.repository.RepositoryException;
import model.User;
import java.util.List;

public interface UserRepository {
    void save(User user) throws RepositoryException;

    User findById(int id) throws RepositoryException;

    User findByUsername(String username) throws RepositoryException;

    User findByEmail(String email) throws RepositoryException;

    List<User> findAll() throws RepositoryException;

    void deleteById(int id) throws RepositoryException;
}
