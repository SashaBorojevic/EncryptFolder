package com.example.encryptfolder.ui.accountSettings;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.encryptfolder.CreateAccount;
import com.example.encryptfolder.R;
import com.example.encryptfolder.databinding.FragmentGalleryBinding;
import com.example.encryptfolder.ui.Database.AESCrypt;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class AccountSettingsFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private TextInputLayout firstName, lastName, email, phone, userName, password, confirmpassword;
    DBHelper db;
    String EncryptedPassword = "";
    AESCrypt encrypt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        email = root.findViewById(R.id.email);
        phone = root.findViewById(R.id.phoneNumber);
        userName = root.findViewById(R.id.createUsername);
        password = root.findViewById(R.id.password);
        confirmpassword = root.findViewById(R.id.confirmPassword);
        Button updateAccount = root.findViewById(R.id.update);

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        encrypt = new AESCrypt();
        db = new DBHelper(getActivity());

        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FirstName = firstName.getEditText().getText().toString();
                String LastName = lastName.getEditText().getText().toString();
                String Email = email.getEditText().getText().toString();
                String Phone = phone.getEditText().getText().toString();
                String UserName = userName.getEditText().getText().toString();
                String Password = password.getEditText().getText().toString();
                String ConfirmPassword = confirmpassword.getEditText().getText().toString();
                // validating if the text fields are empty or not.
                if (!Email.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        Toast.makeText(getActivity(), "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (db.isUsernameValid(UserName) == false) {
                    Toast.makeText(getActivity(), "Username already used!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Password.isEmpty()){
                    if (Password.length() < 8) {
                        Toast.makeText(getActivity(), "Password length must have at least 8 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!UpperCasePatten.matcher(Password).find()) {
                        Toast.makeText(getActivity(), "Password must have at least 1 uppercase character!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!lowerCasePatten.matcher(Password).find()) {
                        Toast.makeText(getActivity(), "Password must have at least one lowercase character !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!digitCasePatten.matcher(Password).find()) {
                        Toast.makeText(getActivity(), "Password must have at least one digit!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!Password.equals(ConfirmPassword)) {
                        Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                try {
                    if (!Password.isEmpty()) {
                        //EncryptedPassword = encrypt.Encrypt(UserName ,Password);
                    }
                    db.updateUser(SaveSharedPreference.getUserName(getContext()),UserName, EncryptedPassword, FirstName, LastName, Email, Phone);
                    Log.d("Encrypted Password",EncryptedPassword);
                    Log.d("Password",Password);
                    Toast.makeText(getActivity(), "User profile updated!", Toast.LENGTH_SHORT).show();

                    db.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}