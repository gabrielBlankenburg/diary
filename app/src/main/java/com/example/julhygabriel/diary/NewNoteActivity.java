package com.example.julhygabriel.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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



    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Inflate the menu; this adds items to the action bar if it is present.
        if (token != "") {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_deslogado, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        switch (item.getItemId()) {
            case R.id.criarNota:
                Intent intentNewNote = new Intent(this, NewNoteActivity.class);
                this.startActivity(intentNewNote);
                break;
            case R.id.sairConta:
                Intent intentLogout = new Intent(this, LogoutActivity.class);
                this.startActivity(intentLogout);
                break;
            case R.id.logarConta:
                Intent intentLogin = new Intent(this, LogoutActivity.class);
                this.startActivity(intentLogin);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
