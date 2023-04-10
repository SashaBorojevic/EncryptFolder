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
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.DeCryptor;
import com.example.encryptfolder.ui.Database.EnCryptor;
import com.example.encryptfolder.ui.Database.SaltedHash;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.sql.Blob;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CreateAccount extends AppCompatActivity {

    private TextInputLayout firstName, lastName, email, phone, userName, password, confirmpassword;
    DBHelper db;
    SaltedHash Sl;
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
        confirmpassword = findViewById(R.id.confirmPassword);
        Button newAccount = findViewById(R.id.makeNewAccount);

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String FirstName = firstName.getEditText().getText().toString().trim();
                final String LastName = lastName.getEditText().getText().toString().trim();
                final String Email = email.getEditText().getText().toString().trim();
                final String Phone = phone.getEditText().getText().toString().trim();
                final String UserName = userName.getEditText().getText().toString().trim();
                final String Password = password.getEditText().getText().toString().trim();
                final String ConfirmPassword = confirmpassword.getEditText().getText().toString().trim();
                Sl = new SaltedHash();
                db = new DBHelper(CreateAccount.this);
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
                if (Password.length() < 8) {
                    Toast.makeText(CreateAccount.this, "Password length must have at least 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!UpperCasePatten.matcher(Password).find()) {
                    Toast.makeText(CreateAccount.this, "Password must have at least 1 uppercase character!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!lowerCasePatten.matcher(Password).find()) {
                    Toast.makeText(CreateAccount.this, "Password must have at least one lowercase character !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!digitCasePatten.matcher(Password).find()) {
                    Toast.makeText(CreateAccount.this, "Password must have at least one digit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)){
                    Toast.makeText(CreateAccount.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setMessage("RECOMMEND: Please add security questions to your account in the Account Settings in order to recover account in case of a forgotten password." +
                        " Are you sure you want to create a new account?");
                builder.setPositiveButton("Yes, Log in!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            String Salt = Sl.getSalt();
                            String pepper = Sl.getPepper();
                            String SaltedHashPassword = Sl.hashPassword(Salt+Password+pepper);
                            db.AddUser(UserName, SaltedHashPassword, FirstName, LastName, Email, Phone, Salt);
                            db.close();
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                        finish();
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.close();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                db.close();
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public void onResume() {
        super.onResume();;
        CreateAccount.this.getSupportActionBar().setTitle("Create an Account");
    }
}
