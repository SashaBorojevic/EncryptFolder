package com.example.encryptfolder.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.encryptfolder.ui.Database.DBHelper;
import com.example.encryptfolder.ui.Database.EnCryptor;

public class ImageModel {
    public byte [] image;
    public String dateAdded;
    public String docName;

    public ImageModel(byte[] image, String date, String name){
        this.image = image;
        this.dateAdded = date;
        this.docName = name;
    }

    public byte[] getImageBytes(){
        return image;
    }
    public Bitmap getImageBitMap(){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public String getDateCreated(){
        return "Date Added: " + dateAdded;
    }
    public String getDocumentName(){
        return docName;
    }
}
