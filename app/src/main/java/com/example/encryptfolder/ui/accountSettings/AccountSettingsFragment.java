package com.example.encryptfolder.ui.accountSettings;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.SaltedHash;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class AccountSettingsFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private TextInputLayout answer1,answer2, email, password, confirmpassword;
    DBHelper db;
    SaltedHash Sl;
    Spinner dropdown1, dropdown2;
    String SaltedHashPassword = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmpassword = root.findViewById(R.id.confirmPassword);
        answer1 = root.findViewById(R.id.answer1);
        answer2 = root.findViewById(R.id.answer2);
        Button updateAccount = root.findViewById(R.id.update);
        dropdown1 = root.findViewById(R.id.spinner1);
        dropdown2 = root.findViewById(R.id.spinner2);


        String[] items1 = new String[]{"In what city were you born?",
                "What is the name of your favorite pet?",
                "What is your mother's maiden name?",
                "What high school did you attend?",
                "What was the name of your elementary school?"};
        String[] items2 = new String[]{"What was the make of your first car?",
                "What was your favorite food as a child?",
                "Where did you meet your spouse?",
                "What year was your father (or mother) born?"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown1.setAdapter(adapter1);
        dropdown2.setAdapter(adapter2);

        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");


        updateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sl = new SaltedHash();
                db = new DBHelper(getActivity());
                String Email = email.getEditText().getText().toString().trim();
                String Password = password.getEditText().getText().toString().trim();
                String ConfirmPassword = confirmpassword.getEditText().getText().toString().trim();
                String Answer1 = answer1.getEditText().getText().toString().trim();
                String Answer2 = answer2.getEditText().getText().toString().trim();
                String text1 = dropdown1.getSelectedItem().toString();
                String text2 = dropdown2.getSelectedItem().toString();
                // validating if the text fields are empty or not.
                if (Email.isEmpty() && Password.isEmpty() && Answer1.isEmpty() && Answer2.isEmpty()){
                    Toast.makeText(getActivity(), "All fields are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Answer1.isEmpty() && !Answer2.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide an answer for Security Question 1", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Answer1.isEmpty() && Answer2.isEmpty()){
                    Toast.makeText(getActivity(), "Please provide an answer for Security Question 2", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Email.isEmpty()){
                    if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                        Toast.makeText(getActivity(), "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
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
                if (Answer1.isEmpty() && Answer2.isEmpty()){
                    text1 = "";
                    text2 = "";
                }
                try {
                    if (!Password.isEmpty()) {
                        String Salt = db.getUserSalt(SaveSharedPreference.getUserName(getContext()));
                        String pepper = Sl.getPepper();
                        SaltedHashPassword = Sl.hashPassword(Salt+Password+pepper);
                    }
                    db.updateUser(SaveSharedPreference.getUserName(getContext()), SaltedHashPassword, Email, text1, text2, Answer1, Answer2);
                    db.close();
                    Toast.makeText(getActivity(), "User profile updated!", Toast.LENGTH_SHORT).show();
                    email.getEditText().getText().clear();
                    password.getEditText().getText().clear();
                    confirmpassword.getEditText().getText().clear();
                    answer1.getEditText().getText().clear();
                    answer2.getEditText().getText().clear();
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