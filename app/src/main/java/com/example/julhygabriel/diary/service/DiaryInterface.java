package com.example.julhygabriel.diary.service;

import com.example.julhygabriel.diary.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DiaryInterface {
    @GET("diary")
    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App"
    })
    Call<List<Note>> listNotes(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("diary")
    @Headers({
            "Accept: application/json",
            "User-Agent: Retrofit-Sample-App"
    })
    Call<Note> postNote(@Header("Authorization") String token,
                               @Field("title") String title,
                               @Field("label") String label,
                               @Field("content") String content);
}
