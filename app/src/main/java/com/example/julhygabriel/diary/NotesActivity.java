package com.example.julhygabriel.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.julhygabriel.diary.adapter.CustomAdapter;
import com.example.julhygabriel.diary.model.Note;
import com.example.julhygabriel.diary.service.DiaryInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token == "") {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.11/diary/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DiaryInterface service = retrofit.create(DiaryInterface.class);

        String auth = "Bearer "+token;

        Call<List<Note>> call = service.listNotes(auth);

        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.code() == 200) {
                    List<Note> resp = response.body();
                    for (Note notes : resp) {
                        Log.d("Sucesso", String.valueOf(notes.getId()));
                        generateDataList(response.body());
                    }
                } else {
                    Toast.makeText(NotesActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                Toast.makeText(NotesActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("Error", String.valueOf(t));
            }
        });
    }

    private void generateDataList(List<Note> photoList) {
        RecyclerView recyclerView = findViewById(R.id.customRecyclerView);
        CustomAdapter adapter = new CustomAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(NotesActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void logout(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", "");

        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
