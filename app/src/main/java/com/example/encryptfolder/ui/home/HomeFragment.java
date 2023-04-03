package com.example.encryptfolder.ui.home;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptfolder.R;
import com.example.encryptfolder.databinding.FragmentHomeBinding;
import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.EnCryptor;
import com.example.encryptfolder.ui.Database.SaveSharedPreference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements ImagesRecylerViewInterface{

    private FragmentHomeBinding binding;
    EnCryptor decrypt;
    ArrayList<ImageModel> imageModels = new ArrayList<>();

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setImageModels();

        recyclerView = view.findViewById(R.id.imageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        ImagesRecyclerViewAdapter adapter = new ImagesRecyclerViewAdapter(getContext(), imageModels, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onItemClick(int position, ArrayList<ImageModel> models) {
        Fragment someFragment = new ViewImageFragment(imageModels.get(position));
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, someFragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    private void setImageModels() {

        imageModels.clear();
        decrypt = new EnCryptor();
        DBHelper DB = new DBHelper(getActivity());
        Cursor cursor = DB.getAllDocuments(SaveSharedPreference.getUserName(getContext()));

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Images Found", Toast.LENGTH_SHORT).show();
            return;

        } else {
            while (cursor.moveToNext()) {
                ImageModel image = null;
                try {
                    image = new ImageModel(
                            decrypt.Decrypt(cursor.getBlob(1), DB.getUserSalt(SaveSharedPreference.getUserName(getContext()))),
                            cursor.getString(3),
                            cursor.getString(4));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                imageModels.add(image);
            }
        }
        DB.close();
    }
    @Override
    public void onResume() {
        super.onResume();;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home - All Images");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}