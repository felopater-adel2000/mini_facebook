package com.faculty.minifbapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MiniFBAPI {

    //Get all posts
    @GET("/posts/getAllPosts")
    Call<List<Post>> getAllPosts();

    //Add new post
    @POST("/posts/createNewPost")
    Call<Post> addNewPost(@Body Post post);

    //User login
    @POST("/users/login")
    Call<LoginSignUpResponse> login(@Body User user);

    //User SignUp
    @POST("/users/signUp")
    Call<LoginSignUpResponse> signUp(@Body User user);

}
