package com.example.encryptfolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        EditText user = findViewById(R.id.username);
        EditText pass = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        Button createAccount = findViewById(R.id.createAccount);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        loading.setVisibility(View.GONE);
    }
}