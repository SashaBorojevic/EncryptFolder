package com.example.encryptfolder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.encryptfolder.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

public class CreateAccount extends AppCompatActivity {

    private TextInputLayout firstName, lastName, email, phone, userName, password, confirmassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phoneNumber);
        userName = findViewById(R.id.createUsername);
        password = findViewById(R.id.password);
        confirmassword = findViewById(R.id.confirmPassword);

        Button newAccount = findViewById(R.id.makeNewAccount);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setMessage("Are you sure you want to create a new account?");
                builder.setPositiveButton("Yes, Log in", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}