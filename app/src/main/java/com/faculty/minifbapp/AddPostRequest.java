package com.faculty.minifbapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddPostRequest implements Serializable {

    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("user")
    @Expose
    private User user;
    private final static long serialVersionUID = -1434390869486182849L;

    /**
     * No args constructor for use in serialization
     */
    public AddPostRequest() {
    }

    /**
     * @param body
     * @param user
     */
    public AddPostRequest(String body, User user) {
        super();
        this.body = body;
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
