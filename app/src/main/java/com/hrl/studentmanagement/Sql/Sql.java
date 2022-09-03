package com.hrl.studentmanagement.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Sql extends SQLiteOpenHelper {
    public Sql(Context context) {
        super(context, "student.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE studentinfo(_id INTEGER PRIMARY KEY AUTOINCREMENT ,name VARCHAR(15),age VARCHAR(15),sex VARCHAR(4),aihao VARCHAR(25),phone VARCHAR(15),date VARCHAR(20))");
        db.execSQL("CREATE TABLE admininfo(_id INTEGER PRIMARY KEY AUTOINCREMENT ,username VARCHAR(50),password VARCHAR(50),phone VARCHAR(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
