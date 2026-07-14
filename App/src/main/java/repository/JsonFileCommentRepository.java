package repository;

import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import log.LogManager;
import model.Comment;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

public class JsonFileCommentRepository implements CommentRepository {
    private static JsonFileCommentRepository instance;
    private final List<Comment> comments;

    private static final String FILE_PATH = "data/comments.json";
    private final Gson gson;

    private JsonFileCommentRepository() {
        gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        comments = loadFromFile();
    }

    public static JsonFileCommentRepository getInstance() {
        if (instance == null) {
            instance = new JsonFileCommentRepository();
        }
        return instance;
    }

    private List<Comment> loadFromFile(){
        File file = new File(FILE_PATH);
        if(!file.exists()){ return new ArrayList<>(); }

        try (Reader reader = new BufferedReader(new FileReader(file))){
            Type listType = new TypeToken<ArrayList<Comment>>(){}.getType();
            List<Comment> comments = gson.fromJson(reader,listType);
            if(comments.isEmpty()){ return new ArrayList<>(); }
            else { return comments; }
        } catch (IOException e){
            LogManager.getInstance().log("Failed to load comments: " + e.getMessage());
            return new ArrayList<>();

        }
    }

    private void saveToFile(){
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();

        try (Writer writer = new BufferedWriter(new FileWriter(file))){
            gson.toJson(comments, writer);
        } catch (IOException e){
            LogManager.getInstance().log("Failed to save comments: " + e.getMessage());
        }
    }

    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
        saveToFile();
    }

    @Override
    public void removeById(int id) {
        boolean removed = comments.removeIf(comment -> comment.getID() == id);
        if (removed) saveToFile();
    }

    @Override
    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public Comment findById(int id) {
        for (Comment comment : comments) {
            if (comment.getID() == id){
                return comment;
            }
        }

        return null;
    }

    @Override
    public List<Comment> getCommentsByUser(int userId) {
        List<Comment> userComments = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getOwnerID() == userId) {
                userComments.add(comment);
            }
        }

        return userComments;
    }

    public void update(Comment comment) {
        saveToFile();
    }
}
