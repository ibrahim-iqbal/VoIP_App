package com.example.voipapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbManager extends SQLiteOpenHelper
{
    DbManager(@Nullable Context context)
    {
        super(context, "Demodb.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query="create table reg( id INTEGER primary key autoincrement ,name text , userid varchar , email varchar, mobno varchar, dob text, secq varchar,seca varchar, password varchar)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
