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

import com.example.encryptfolder.R;
import com.example.encryptfolder.databinding.FragmentAddNewDocumentBinding;

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

    Bitmap image = null;
    ImageView preview;

    TextView dateAdded;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (root != null) {
            return root;
        }
        binding = FragmentAddNewDocumentBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        preview = root.findViewById(R.id.imagePreview);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateAdded = root.findViewById(R.id.currentDate);
        dateAdded.setText("Date Added: "+date);
        Button chooseIMage = root.findViewById(R.id.choose_image);
        chosenImage = root.findViewById(R.id.imageView);
        Button takePicture = root.findViewById(R.id.take_picture);

        ActivityResultLauncher<PickVisualMediaRequest> gfgMediaPicker = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), uri);
                    preview.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                Log.d("Opened the picker", "Select something geek");
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

        /*addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*///*");
                //chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                //startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            //}
        //});

        return root;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            preview.setImageBitmap(imageBitmap);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}