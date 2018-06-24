package com.example.julhygabriel.diary.model;

import com.google.gson.annotations.SerializedName;

public class Note {
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("label")
    private String label;
    @SerializedName("content")
    private String content;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLabel() {
        return label;
    }

    public String getContent() {
        return content;
    }
}
