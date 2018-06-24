package com.example.julhygabriel.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.julhygabriel.diary.model.Login;
import com.example.julhygabriel.diary.model.Note;
import com.example.julhygabriel.diary.service.DiaryInterface;
import com.example.julhygabriel.diary.service.LoginInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewNoteActivity extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        this.token = sharedPreferences.getString("token", "");

        if (this.token == "") {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        this.token = "Bearer "+this.token;
    }

    public void post(View view) {
        EditText edtTitle = (EditText)findViewById(R.id.title);
        EditText edtLabel = (EditText)findViewById(R.id.label);
        EditText edtContent = (EditText)findViewById(R.id.content);

        String title = edtTitle.getText().toString();
        String label = edtLabel.getText().toString();
        String content = edtContent.getText().toString();

        Log.d("title", title);
        Log.d("label", label);
        Log.d("content", content);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.11/diary/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DiaryInterface service = retrofit.create(DiaryInterface.class);
        Log.d("primeiro", this.token);
        Call<Note> call = service.postNote(this.token, title, label, content);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                Note resp = response.body();
                if (response.code() == 200) {
                    Toast.makeText(NewNoteActivity.this, "Content addedd successfully", Toast.LENGTH_SHORT).show();
                    loadNotes();

                } else {
                    Log.d("token", token);
                    Log.d("response code", String.valueOf(response.code()));
                    Toast.makeText(NewNoteActivity.this, "Error adding content", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                Toast.makeText(NewNoteActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("Error", String.valueOf(t));
            }
        });
    }

    public void loadNotes() {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }
}
