package com.example.encryptfolder.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encryptfolder.R;

import java.util.ArrayList;


public class ImagesRecyclerViewAdapter extends RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder> {
    private final ImagesRecylerViewInterface ViewInterface;

    Context context;
    ArrayList<ImageModel> images;


    public ImagesRecyclerViewAdapter(Context context, ArrayList<ImageModel> images,
                                       ImagesRecylerViewInterface RecylerViewInterface){
        this.context = context;
        this.images = images;
        this.ViewInterface = RecylerViewInterface;
    }

    @NonNull
    @Override
    public ImagesRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_item_layout, parent, false);

        return new ImagesRecyclerViewAdapter.MyViewHolder(view, ViewInterface, images);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesRecyclerViewAdapter.MyViewHolder holder, int position) {
        // assign values as they come on screen

        holder.image.setImageBitmap(images.get(position).getImageBitMap());
        holder.date.setText(images.get(position).getDateCreated());
        holder.documentName.setText(images.get(position).getDocumentName());
    }

    @Override
    public int getItemCount() {
            return images.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView date, documentName;


        public MyViewHolder(@NonNull View itemView, ImagesRecylerViewInterface ImageRecylerViewInterface,
                            ArrayList<ImageModel> allImages) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.dateAdded);
            documentName = itemView.findViewById(R.id.imageName);


            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(ImageRecylerViewInterface != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            ImageRecylerViewInterface.onItemClick(pos, allImages);
                        }
                    }
                }
            });

        }
    }
}
