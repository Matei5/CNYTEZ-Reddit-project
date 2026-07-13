package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exceptions.repository.RepositoryException;
import model.User;
import log.LogManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class JsonFileUserRepository implements UserRepository {

    private static JsonFileUserRepository instance;
    private static final String FILE_PATH = "data/users.json";

    private final List<User> users;
    private final Gson gson;

    private JsonFileUserRepository() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        users = new ArrayList<>();
        loadFromFile();
    }

    public static JsonFileUserRepository getInstance() {
        if (instance == null) {
            instance = new JsonFileUserRepository();
        }

        return instance;
    }

    private List<User> loadFromFile() {
        File file = new File(FILE_PATH);
        if(!file.exists()){ return new ArrayList<>(); }

        try (Reader reader = new BufferedReader(new FileReader(file))){
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            List<User> users = gson.fromJson(reader,listType);
            if(users.isEmpty()){ return new ArrayList<>(); }
            else { return users; }
        } catch (IOException e){
            LogManager.getInstance().log("Failed to load users: " + e.getMessage());
            return new ArrayList<>();

        }
    }

    private void saveToFile() throws RepositoryException {
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (Writer writer = new BufferedWriter(new FileWriter(file))){
            gson.toJson(users, writer);
        } catch (IOException e){
            throw new RepositoryException("Failed to save users: " + e.getMessage());
        }
    }

    @Override
    public void save(User user) throws RepositoryException {
        users.add(user);
        saveToFile();
    }

    @Override
    public User findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }

    @Override
    public void deleteById(int id) throws RepositoryException {
        users.removeIf(user -> user.getId() == id);
        saveToFile();
    }
}
