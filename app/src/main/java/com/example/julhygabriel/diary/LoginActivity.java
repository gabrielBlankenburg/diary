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
import com.example.julhygabriel.diary.service.LoginInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText edtEmail = (EditText)findViewById(R.id.email);
        EditText edtPassword = (EditText)findViewById(R.id.password);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.11/diary/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginInterface service = retrofit.create(LoginInterface.class);

        Call<Login> call = service.login(email, password);

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
                        Toast.makeText(LoginActivity.this, resp.getResponse(), Toast.LENGTH_SHORT).show();
                        loadNotes();
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid login", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.d("Error", String.valueOf(t));
            }
        });
    }

    public void loadNotes() {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }
}
