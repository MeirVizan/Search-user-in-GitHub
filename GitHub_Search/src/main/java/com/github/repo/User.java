package com.github.repo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * User class
 * the Columns of database (ID, Username, Followers, Count)
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * username in GitHub
     */
    private String username;
    /**
     * followers in GitHub
     */
    private int followers;

    /**
     * count of history searching
     */
    private int count = 0;

    /**
     * Default C'tor
     */
    public User() {}

    /**
     * Ctor
     * @param username init username
     * @param follower init follower
     * @param count init count
     */
    public User(String username, int follower, int count) {
        this.username = username;
        this.followers = follower;
        this.count = 0;
    }

    /**
     * setId function
     * @param id of GitHub users
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getId function
     * @return the ID of Users
     */
    public long getId() {
        return id;
    }

    /**
     * setUsername function
     * @param username update the name of GitHub users
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * setFollowers function
     * @param follower update the followers number of users
     */
    public void setFollowers(int follower) {
        this.followers = follower;
    }

    /**
     * getUsername function
     * @return GitHub username
     */
    public String getUsername() {
        return username;
    }

    public int getFollowers() {
        return followers;
    }

    /**
     * setCount function
     * @param count update or init the count
     */
    public void setCount(int count){ this.count += count; }

    /**
     * getCount function
     * @return the count of searching
     */
    public int getCount(){ return count; }

    /**
     * toString function
     * @return the class details
     */
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + username + ", followers=" + followers + ",count=" + count + '}';
    }
}

