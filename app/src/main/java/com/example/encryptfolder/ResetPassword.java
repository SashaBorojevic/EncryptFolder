package com.example.encryptfolder;


import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.SaltedHash;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class ResetPassword extends AppCompatActivity {
    private TextInputLayout Answer1, Answer2, password, confirmpassword;
    TextView Question1,Question2;
    DBHelper db;
    SaltedHash Sl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        db = new DBHelper(ResetPassword.this);
        Answer1 = findViewById(R.id.answer1);
        Answer2 = findViewById(R.id.answer2);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmPassword);
        Button reset = findViewById(R.id.resetPassword);
        Question1 = findViewById(R.id.question1);
        Question2 = findViewById(R.id.question2);

        Question1.setText(db.getSecurityQuestion1(SaveSharedPreference.getUserName(ResetPassword.this)));
        Question2.setText(db.getSecurityQuestion2(SaveSharedPreference.getUserName(ResetPassword.this)));

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String FirstQuestion = Answer1.getEditText().getText().toString().trim();
                final String SecondQuestion = Answer2.getEditText().getText().toString().trim();
                final String Password = password.getEditText().getText().toString().trim();
                final String ConfirmPassword = confirmpassword.getEditText().getText().toString().trim();
                Sl = new SaltedHash();
                // validating if the text fields are empty or not.
                if (FirstQuestion.isEmpty() || SecondQuestion.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()){
                    Toast.makeText(ResetPassword.this, "Please fill out all of the fields!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!FirstQuestion.equals(db.getAnswer1(SaveSharedPreference.getUserName(ResetPassword.this)))){
                    Toast.makeText(ResetPassword.this, "Security Question 1 is incorrect!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!SecondQuestion.equals(db.getAnswer2(SaveSharedPreference.getUserName(ResetPassword.this)))){
                    Toast.makeText(ResetPassword.this, "Security Question 2 is incorrect!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length() < 8) {
                    Toast.makeText(ResetPassword.this, "Password length must have at least 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!UpperCasePatten.matcher(Password).find()) {
                    Toast.makeText(ResetPassword.this, "Password must have at least 1 uppercase character!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!lowerCasePatten.matcher(Password).find()) {
                    Toast.makeText(ResetPassword.this, "Password must have at least one lowercase character !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!digitCasePatten.matcher(Password).find()) {
                    Toast.makeText(ResetPassword.this, "Password must have at least one digit!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.equals(ConfirmPassword)){
                    Toast.makeText(ResetPassword.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    String Salt = db.getUserSalt(SaveSharedPreference.getUserName(ResetPassword.this));
                    String pepper = Sl.getPepper();
                    String SaltedHashPassword = Sl.hashPassword(Salt + Password + pepper);
                    db.updateUser(SaveSharedPreference.getUserName(ResetPassword.this), SaltedHashPassword, "", "", "", "", "");
                    db.close();
                    SaveSharedPreference.setUserName(ResetPassword.this, null);
                    Toast.makeText(ResetPassword.this, "Password has been reset!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
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
        ResetPassword.this.getSupportActionBar().setTitle("Reset Password");
    }
}
