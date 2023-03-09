package com.example.EncryptFolder.ui.Database;

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

    public long AddUser(String firstName,
                                  String lastName,
                                  String email,
                                  String phoneNumber,
                                  boolean opening,
                                  boolean closing) {
        //insert employee into database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
        contentValues.put("lastName", lastName);
        contentValues.put("emailAddress", email);
        contentValues.put("phoneNumber", phoneNumber);

        long result = DB.insert("Users", null, contentValues);
        return result;
    }


    public Boolean updateEmployeeFirstName(int employeeID, String firstName){
        //update employees first name in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstName", firstName);
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

    public Boolean updateEmployeeLastName(int employeeID, String lastName){
        //update employees last name in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lastName", lastName);
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

    public Boolean updateEmployeeEmail(int employeeID, String email){
        //update employees email in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("emailAddress", email);
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

    public Boolean updateEmployeePhone(int employeeID, String phone){
        //update employees phone number in database
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phoneNumber", phone);
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


    public long insertShift(Date date, int timeSlot){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", new SimpleDateFormat("dd-MM-yyyy").format(date));
        contentValues.put("timeSlot", timeSlot);
        contentValues.put("employees", "");
        return DB.insert("SCHEDULEDSHIFTS", null, contentValues);
    }
    

    public void removeFutureShifts (int employeeID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        ContentValues contentValues = new ContentValues();
        Cursor cursor = DB.rawQuery("Select * from SCHEDULEDSHIFTS", null);
        try {
            while (cursor.moveToNext()) {
                Date current = null;
                try {
                    current = formatter.parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (current.after(date)) {
                    String Employees = cursor.getString(3);
                    Employees = Employees.replace(","+employeeID,"");
                    contentValues.put("employees", Employees);
                    long result = DB.update("SCHEDULEDSHIFTS",
                            contentValues,
                            "shiftID=?",
                            new String[] {cursor.getString(0)});
                }
            }
        }
        finally{
                cursor.close();
        }

    }

    public Boolean maxPerShift(int employeeID, int shiftID){
        // Only allow for the assignment of up to three employees per shift
        // Returns true if less than three employees have been assigned to a shift, false otherwise
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from SCHEDULEDSHIFTS where shiftID = ?",
                new String[] {String.valueOf(shiftID)});
        boolean max = true;
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String[] employees = cursor.getString(3).split(",");
            if (employees.length > 3){
                max = false;
            }
        }
        return max;
    }

    public Boolean qualifiedMorningShift(int employeeID, int shiftID){
        // Morning shift must have at least one student qualified for opening
        // Returns true if at least one student qualified for opening
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursorShift = DB.rawQuery("Select * from SCHEDULEDSHIFTS where shiftID = ?",
                new String[] {String.valueOf(shiftID)});
        Cursor cursorQualified = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        boolean qualified = true;
        boolean qualifiedForOpening = cursorQualified.getInt(4) > 0;;
        if((cursorShift.getCount() >= 1) && (!qualifiedForOpening)){
            qualified = false;
        }
        return qualified;
    }

    public Boolean qualifiedAfternoonShift(int employeeID, int shiftID){
        // Afternoon shift must have at least one student qualified for closing
        // Returns true if at least one student qualified for closing
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursorShift = DB.rawQuery("Select * from SCHEDULEDSHIFTS where shiftID = ?",
                new String[] {String.valueOf(shiftID)});
        Cursor cursorQualified = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        boolean qualified = true;
        boolean qualifiedForClosing = cursorQualified.getInt(5) > 0;;
        if((cursorShift.getCount() >= 1) && (!qualifiedForClosing)){
            qualified = false;
        }
        return qualified;
    }

    public Boolean qualifiedFullDayShift(int employeeID, int shiftID){
        // Full-day shift must have at least one student qualified for opening and one student
        // qualified for closing
        // Returns true if at least one student qualified for opening and closing
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursorShift = DB.rawQuery("Select * from SCHEDULEDSHIFTS where shiftID = ?",
                new String[] {String.valueOf(shiftID)});
        Cursor cursorQualified = DB.rawQuery("Select * from EmployeeDetails where employeeID = ?",
                new String[] {String.valueOf(employeeID)});
        boolean qualified = false;
        boolean qualifiedForOpening = cursorQualified.getInt(4) > 0;;
        boolean qualifiedForClosing = cursorQualified.getInt(5) > 0;;
        // More logic needs to be implemented, test the function first
        if((qualifiedForOpening) || (qualifiedForClosing)){
            qualified = true;
        }
        return qualified;
    }

    public Boolean checkDuplicatePhoneNumber(String employeePhoneNumber){
        //returns true if the phone number exists in the database, false otherwise
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from EmployeeDetails where phoneNumber = ?",
                new String[] {employeePhoneNumber});
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
}