package com.example.my;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Connect extends SQLiteOpenHelper {

    public static final String DB_NAME = "My.db";
    public static final String Table_Name1 = "Phrases_Table";
    public static final String Table_Name2 = "Subscription_TABLE";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "PHRASE";
    public static final String COL__1 = "ID";
    public static final String COL__2 = "Subscripted_Languages";


    public DB_Connect(Context context) {
        super( context, DB_NAME, null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" create table " + Table_Name1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, PHRASE TEXT)");
        db.execSQL("create table " + Table_Name2 + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,Subscripted_Languages TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name1);
        db.execSQL("DROP TABLE IF EXISTS "+Table_Name2);
        onCreate(db);
    }

    //Adding data to database
    public boolean savePhrase(String phrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,phrase);
        long output = db.insert(Table_Name1,null ,contentValues);
        if(output == -1)
            return false;
        else
            return true;

    }

    //display data from database
    public Cursor displayData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String getPhrases = "Select * from "+Table_Name1;
        Cursor cursor = db.rawQuery(getPhrases, null);

        return cursor;
    }

    //update to databse
    public boolean updatePhrase(String id, String phrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,phrase);
        db.update(Table_Name1,contentValues,"id = ?",new String[] {id});
        return true;

    }

    public boolean addedLanguageData(String Subscripted_Languages) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL__2,Subscripted_Languages);
        long output = db.insert(Table_Name2,null,contentValues);
        if (output == -1)
            return  false;
        else
            return true;
    }

    public Integer deleteDataLanguages(String languages) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(Table_Name2,"Subscripted_Languages = ?",new String[]{languages});
    }

    public Cursor getLanguageData() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cur = db.rawQuery("select * from "+Table_Name2,null);
        return  cur;
    }


}
