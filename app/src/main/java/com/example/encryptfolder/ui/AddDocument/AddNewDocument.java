package com.example.encryptfolder.ui.AddDocument;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encryptfolder.CreateAccount;
import com.example.encryptfolder.R;
import com.example.encryptfolder.databinding.FragmentAddNewDocumentBinding;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.EnCryptor;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNewDocument extends Fragment {

    private FragmentAddNewDocumentBinding binding;
    private View root;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICKFILE_RESULT_CODE = 8778;
    ImageView chosenImage;
    ImageView cameraImage;
    EnCryptor encrypt;
    Bitmap image = null;
    byte[] imageBytes = null;
    ImageView preview;
    Button saveImage;
    TextView dateAdded;
    private TextInputLayout docName;
    DBHelper db;
    String date;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (root != null) {
            return root;
        }
        binding = FragmentAddNewDocumentBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        preview = root.findViewById(R.id.imagePreview);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateAdded = root.findViewById(R.id.currentDate);
        dateAdded.setText(" Date Added:  "+date);
        saveImage = root.findViewById(R.id.saveImage);
        docName = root.findViewById(R.id.docName);
        Button chooseIMage = root.findViewById(R.id.choose_image);
        chosenImage = root.findViewById(R.id.imageView);
        Button takePicture = root.findViewById(R.id.take_picture);
        db = new DBHelper(getActivity());

        ActivityResultLauncher<PickVisualMediaRequest> gfgMediaPicker = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
                    preview.setImageBitmap(image);
                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
                    // compress Bitmap
                    image.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    // Initialize byte array
                    imageBytes = stream.toByteArray();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        chooseIMage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gfgMediaPicker.launch(new PickVisualMediaRequest.Builder().setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
            }
        });
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encrypt = new EnCryptor();
                final String DocumentName = docName.getEditText().getText().toString().trim();
                if(image == null) {
                    Toast.makeText(getActivity(), "Please choose an Image or Take a picture!", Toast.LENGTH_SHORT).show();return;}
                if(DocumentName.isEmpty()){
                    Toast.makeText(getActivity(), "Please add a Document Name!", Toast.LENGTH_SHORT).show();return;}
                if (imageBytes != null){
                    try {
                        byte[] encryptedBytes = encrypt.Encrypt(imageBytes, db.getUserSalt(SaveSharedPreference.getUserName(getContext())));
                        db.AddDucument(encryptedBytes, SaveSharedPreference.getUserName(getContext()), date, DocumentName);
                        Toast.makeText(getActivity(), "Image added!", Toast.LENGTH_SHORT).show();
                        docName.getEditText().getText().clear();
                        imageBytes = null;
                        preview.setImageResource(0);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
                else {Toast.makeText(getActivity(), "Can not add the image to database", Toast.LENGTH_SHORT).show();return;}
            }
        });
        return root;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            preview.setImageBitmap(image);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            // compress Bitmap
            image.compress(Bitmap.CompressFormat.JPEG,100,stream);
            // Initialize byte array
            imageBytes = stream.toByteArray();
        }
    }

        /*addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*///*");
    //chooseFile = Intent.createChooser(chooseFile, "Choose a file");
    //startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    //}
    //});

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}