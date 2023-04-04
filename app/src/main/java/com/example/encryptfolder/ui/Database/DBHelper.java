package com.example.encryptfolder.ui.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.encryptfolder.ui.home.ImageModel;

import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "EncryptFolder.db", null , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        // create table
        // in future sprint, we will store schedule data
        DB.execSQL("create Table Users(" +
                "username TEXT," +
                "password TEXT," +
                "firstName TEXT," +
                "lastName TEXT," +
                "emailAddress TEXT, " +
                "phoneNumber TEXT, " +
                "salt TEXT)");
        DB.execSQL("create Table Documents(" +
                "imageID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "image BLOB," +
                "username TEXT," +
                "dateAdded TEXT," +
                "DocumentName TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        // drop table if exists
        DB.execSQL("drop Table if EXISTS Users");
        DB.execSQL("drop Table if EXISTS Documents");
        onCreate(DB);
    }

    public long AddUser(String username,String password,String firstName,
                                  String lastName,
                                  String email,
                                  String phoneNumber,
                                  String salt) {
        //insert user into database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("emailAddress", email);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("salt", salt);

        long result = DB.insert("Users", null, contentValues);
        return result;
    }
    public long AddDucument(byte[] image,String username, String date, String docName ) {
        //insert user into database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image);
        contentValues.put("username", username);
        contentValues.put("dateAdded", date);
        contentValues.put("DocumentName", docName);

        long result = DB.insert("Documents", null, contentValues);
        return result;
    }
    public Cursor getAllDocuments(String username){
        //gets all employees that have 1 in the "active" category
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Documents where username = ?", new String[] {String.valueOf(username)});
    }
    public ImageModel getImageByID(int imageID){
        SQLiteDatabase DB = this.getWritableDatabase();
        ImageModel image;
        Cursor cursor = DB.rawQuery("Select * from Documents where imageID = ?",
                new String[] {String.valueOf(imageID)});
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            image = new ImageModel(
                    cursor.getBlob(1),
                    cursor.getString(3),
                    cursor.getString(4)
            );
        } else{
            image = null;
        }
        return image;
}




    public Boolean isUsernameValid(String username){
        //check if username exists already
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where username = ?", new String[] {String.valueOf(username)});
        if(cursor.getCount()>0){
            return false;
        }
        else{
            return true;
        }
    }

    public String getUserPassword(String username){
        //check if username exists already
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where username = ?", new String[] {String.valueOf(username)});
        if (cursor.moveToFirst()) {
            int placeColumn = cursor.getColumnIndex("password");
            String password = cursor.getString(placeColumn);
            return password;
        }
        else{
            return null;
        }
    }
    public Boolean updateUser(String oldUsername, String newPassword, String firstName, String lastName,String email,
                              String phone){
        //update employees email in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!newPassword.isEmpty()){contentValues.put("password", newPassword);}
        if (!firstName.isEmpty()){contentValues.put("firstName", firstName);}
        if (!lastName.isEmpty()){contentValues.put("lastName", lastName);}
        if (!email.isEmpty()){contentValues.put("emailAddress", email);}
        if (!phone.isEmpty()){contentValues.put("phoneNumber", phone);}

        Cursor cursor = DB.rawQuery("Select * from Users where username = ?",
                new String[] {String.valueOf(oldUsername)});
        if(cursor.getCount()>0){
            long result = DB.update("Users",
                    contentValues,
                    "username=?",
                    new String[] {String.valueOf(oldUsername)});
            cursor.close();
            return result != -1;
        }else{
            return false;
        }
    }
    public Boolean addPassword(String username, String password){
        //update employees email in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        Cursor cursor = DB.rawQuery("Select * from Users where username = ?",
                new String[] {String.valueOf(username)});
        if(cursor.getCount()>0){
            long result = DB.update("Users",
                    contentValues,
                    "username=?",
                    new String[] {String.valueOf(username)});
            cursor.close();
            return result != -1;
        }else{
            return false;
        }
    }
    public String getUserSalt(String username){

        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where username = ?", new String[] {String.valueOf(username)});
        if (cursor.moveToFirst()) {
            int placeColumn = cursor.getColumnIndex("salt");
            String salt = cursor.getString(placeColumn);
            return salt;
        }
        else{
            return null;
        }
    }
}