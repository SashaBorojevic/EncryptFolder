package com.example.encryptfolder.ui.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
                "phoneNumber TEXT)");
        DB.execSQL("create Table Documents(" +
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

    public long AddUser(String username, String password,String firstName,
                                  String lastName,
                                  String email,
                                  String phoneNumber) {
        //insert employee into database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("emailAddress", email);
        contentValues.put("phoneNumber", phoneNumber);

        long result = DB.insert("Users", null, contentValues);
        return result;
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

    public Boolean updateEmployeeOpening(int employeeID, boolean opening){
        //update employees opening training in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qualifiedForOpening", opening);
        Cursor cursor = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        if(cursor.getCount()>0){
            long result = DB.update("EmployeeDetails",
                    contentValues,
                    "employeeID=?",
                    new String[] {String.valueOf(employeeID)});
            cursor.close();
            return result != -1;
        }else{
            return false;
        }
    }

    public Boolean updateEmployeeClosing(int employeeID, boolean closing){
        //update employees closing training in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qualifiedForClosing", closing);
        Cursor cursor = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        if(cursor.getCount()>0){
            long result = DB.update("EmployeeDetails",
                    contentValues,
                    "employeeID=?",
                    new String[] {String.valueOf(employeeID)});
            cursor.close();
            return result != -1;
        }else{
            return false;
        }
    }

    public Boolean deleteEmployee(int employeeID){
        //delete employee from database
        //finds an employee in the database with a given ID and change the value of "active" from 1 to 0
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        boolean success = true;
        contentValues.put("isActive", 0);
        Cursor cursor = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        Cursor cursor2 = DB.rawQuery("Select * from EMPLOYEEAVAILIBILITY where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        if(cursor.getCount()>0){
            long result = DB.update("EmployeeDetails",
                    contentValues,
                    "employeeID=?",
                    new String[] {String.valueOf(employeeID)});
            cursor.close();
            success = result != -1;
        } else{
            success = false;
        }
        if(cursor2.getCount()>0){
            long result = DB.update("EMPLOYEEAVAILIBILITY",
                    contentValues,
                    "employeeID=?",
                    new String[] {String.valueOf(employeeID)});
            cursor.close();
            success = success && result != -1;
        } else {
            success = false;
        }
        return success;
    }


    public Cursor getActiveEmployees(){
        //gets all employees that have 1 in the "active" category
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from EmployeeDetails where isActive = 1", null);
    }
}