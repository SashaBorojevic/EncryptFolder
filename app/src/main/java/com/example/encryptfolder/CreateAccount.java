package com.example.encryptfolder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.encryptfolder.ui.Database.AESCrypt;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CreateAccount extends AppCompatActivity {

    private TextInputLayout firstName, lastName, email, phone, userName, password, confirmassword;
    DBHelper db;
    AESCrypt encrypt;
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

        encrypt = new AESCrypt();
        db = new DBHelper(CreateAccount.this);
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FirstName = firstName.getEditText().getText().toString();
                String LastName = lastName.getEditText().getText().toString();
                String Email = email.getEditText().getText().toString();
                String Phone = phone.getEditText().getText().toString();
                String UserName = userName.getEditText().getText().toString();
                String Password = password.getEditText().getText().toString();
                String ConfirmPassword = confirmassword.getEditText().getText().toString();
                // validating if the text fields are empty or not.
                if (!Email.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        Toast.makeText(CreateAccount.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (UserName.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                    Toast.makeText(CreateAccount.this, "Please fill out all the required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (db.isUsernameValid(UserName) == false) {
                    Toast.makeText(CreateAccount.this, "Username already used!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)){
                    Toast.makeText(CreateAccount.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String EncryptedPassword = encrypt.Encrypt(Password);
                    db.AddUser(UserName, EncryptedPassword, FirstName, LastName, Email, Phone);
                    Log.d("Encrypted Password",EncryptedPassword);
                    Log.d("Password",Password);
                    db.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setMessage("Are you sure you want to create a new account?");
                builder.setPositiveButton("Yes, Log in!", new DialogInterface.OnClickListener() {
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
    @Override
    public void onBackPressed() {
        finish();
    }

}
