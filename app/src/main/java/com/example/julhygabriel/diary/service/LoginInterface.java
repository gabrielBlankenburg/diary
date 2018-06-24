package com.example.julhygabriel.diary.service;

import com.example.julhygabriel.diary.model.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginInterface {
    @FormUrlEncoded
    @POST("login")
    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App"
    })
    Call<Login> login(@Field("email") String email, @Field("password") String password);
}
