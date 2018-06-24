package com.example.julhygabriel.diary.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("response")
    private String response;
    @SerializedName("accessToken")
    private String accessToken;

    public String getResponse() {
        return response;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
