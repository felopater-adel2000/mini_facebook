
package com.faculty.minifbapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginSignUpResponse implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private User user;
    private final static long serialVersionUID = 7577287283548985357L;

    /**
     * No args constructor for use in serialization
     */
    public LoginSignUpResponse() {
    }

    /**
     * @param user
     * @param message
     */
    public LoginSignUpResponse(String message, User user) {
        super();
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
