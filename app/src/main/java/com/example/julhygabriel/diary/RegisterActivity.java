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
import com.example.julhygabriel.diary.service.RegisterInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void register(View view) {
        EditText edtName = (EditText)findViewById(R.id.name);
        EditText edtEmail = (EditText)findViewById(R.id.email);
        EditText edtPassword = (EditText)findViewById(R.id.password);

        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.11/diary/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterInterface service = retrofit.create(RegisterInterface.class);

        Call<Login> call = service.register(name, email, password);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login resp = response.body();
                if (response.code() == 200) {
                    Log.d("Sucesso", resp.getAccessToken());
                    SharedPreferences sharedPreferences = getSharedPreferences("credentials", Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", resp.getAccessToken());

                    if (editor.commit()) {
                        Toast.makeText(RegisterActivity.this, resp.getResponse(), Toast.LENGTH_SHORT).show();
                        loadNotes();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
