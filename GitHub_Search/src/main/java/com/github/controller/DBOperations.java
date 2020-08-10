package com.github.controller;

import com.github.repo.User;
import com.github.repo.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * DBOperation class
 * is class for operation in our database
 */
public class DBOperations {

    /**
     * Constructor of DBOperations
     */
    public DBOperations(){ }

    /**
     * saveData function with synchronized
     * @param user in database
     * @param json for followers details
     * @param username to save the username of GitHub
     * @param repo for query function
     * @throws JSONException JsonObject Exception
     */
    public static synchronized void saveData(User user, JSONObject json, String username, UserRepository repo) throws JSONException {
        user.setFollowers((int) json.get("followers"));
        user.setCount(1);
        user.setUsername(username);
        repo.save(user);
    }

    /**
     * updateData function with synchronized
     * @param user update the counter
     * @param repo for query function
     */
    public static synchronized void updateData(User user,UserRepository repo){
        user.setCount(1);
        repo.save(user);
    }
}
