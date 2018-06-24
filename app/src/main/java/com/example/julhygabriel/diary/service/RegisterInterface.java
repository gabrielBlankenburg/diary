package com.example.julhygabriel.diary.service;

import com.example.julhygabriel.diary.model.Login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterInterface {
    @FormUrlEncoded
    @POST("register")
    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App"
    })
    Call<Login> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
}
