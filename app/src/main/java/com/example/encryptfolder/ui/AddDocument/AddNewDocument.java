package com.example.encryptfolder.ui.AddDocument;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.example.encryptfolder.R;
import com.example.encryptfolder.databinding.FragmentAddNewDocumentBinding;

public class AddNewDocument extends Fragment {

    private FragmentAddNewDocumentBinding binding;
    private View root;
    private static final int CAMERA_REQUEST = 1888;
    private static final int PICKFILE_RESULT_CODE = 8778;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (root != null) {
            return root;
        }
        binding = FragmentAddNewDocumentBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Button chooseIMage = root.findViewById(R.id.choose_image);
        ImageView chosenImage = root.findViewById(R.id.imageView);
        Button takePicture = root.findViewById(R.id.take_picture);
        Button addDocument = root.findViewById(R.id.choose_document);
        ActivityResultLauncher<PickVisualMediaRequest> gfgMediaPicker = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                chosenImage.setImageURI(uri);
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
        addDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
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