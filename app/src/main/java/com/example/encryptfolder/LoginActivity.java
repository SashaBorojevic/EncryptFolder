package com.example.encryptfolder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.encryptfolder.ui.Database.AESCrypt;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.DeCryptor;
import com.example.encryptfolder.ui.Database.EnCryptor;
import com.example.encryptfolder.ui.Database.SaltedHash;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class LoginActivity extends AppCompatActivity {
    ProgressBar loading;
    EditText user;
    EditText pass;
    SaltedHash Sl;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        Button createAccount = findViewById(R.id.createAccount);
        db = new DBHelper(LoginActivity.this);
        Sl = new SaltedHash();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = user.getText().toString();
                String password = pass.getText().toString();
                String userPassword = db.getUserPassword(userName);
                if (userName.isEmpty() && password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter a valid Username and Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userName.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter a valid Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userPassword!=null){
                    try {
                        String hash = Sl.hashPassword(password);
                        String salt = db.getUserSalt(userName);
                        String UserEnteredPassword = salt + hash;
                        Log.d("Database Password: ",userPassword);
                        Log.d("User entered password: ",UserEnteredPassword);
                        if(UserEnteredPassword.equals(userPassword)) {
                            loading.setVisibility(View.VISIBLE);
                            SaveSharedPreference.setUserName(LoginActivity.this, userName);
                            Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            db.close();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Invalid Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        user.getText().clear();
        pass.getText().clear();

    }
}