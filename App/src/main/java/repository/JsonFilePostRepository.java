package repository;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import model.Post;
import log.LogManager;
import model.Subreddit;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class JsonFilePostRepository implements PostRepository {
    private static JsonFilePostRepository instance;
    private final List<Post> posts;

    private static final String FILE_PATH = "data/posts.json";
    private final Gson gson;

    private JsonFilePostRepository() {
        gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        posts = loadFromFile();
    }

    public static JsonFilePostRepository getInstance() {
        if (instance == null) {
            instance = new JsonFilePostRepository();
        }

        return instance;
    }

    private List<Post> loadFromFile(){
        File file = new File(FILE_PATH);
        if(!file.exists()){ return new ArrayList<>(); }

        try (Reader reader = new BufferedReader(new FileReader(file))){
            Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
            List<Post> posts = gson.fromJson(reader,listType);
            if(posts.isEmpty()){ return new ArrayList<>(); }
            else { return posts; }
        } catch (IOException e){
            LogManager.getInstance().log("Failed to load posts: " + e.getMessage());
            return new ArrayList<>();

        }
    }

    private void saveToFile(){
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (Writer writer = new BufferedWriter(new FileWriter(file))){
            gson.toJson(posts, writer);
        } catch (IOException e){
            LogManager.getInstance().log("Failed to save subreddits: " + e.getMessage());
        }
    }

    @Override
    public void addPost(Post post) {
        posts.add(post);
        saveToFile();
    }

    @Override
    public Post findById(int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }

    @Override
    public boolean deleteById(int id) {
        boolean removed = posts.removeIf(post -> post.getId() == id);
        if (removed){
            saveToFile();
        }
        return removed;
    }

    @Override
    public List<Post> getAllPosts() {
        return posts;
    }

    @Override
    public List<Post> getPostsByUser(int userId) {
        List<Post> userPosts = new ArrayList<>();

        for (Post post : posts) {
            if (post.getOwnerId() == userId) {
                userPosts.add(post);
            }
        }

        return userPosts;
    }
}
