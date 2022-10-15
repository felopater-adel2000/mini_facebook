package com.faculty.minifbapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://minifbapp.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static MiniFBAPI miniFBAPI = retrofit.create(MiniFBAPI.class);

    public static MiniFBAPI getMiniFBAPI() {
        return miniFBAPI;
    }

}
