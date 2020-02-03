package com.example.voipapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ImgManager extends SQLiteOpenHelper
{
    ImgManager(@Nullable Context context)
    {
        super(context, "ImgDb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String img="create table img(id integer  primary key AUTOINCREMENT ,email varchar, img varchar not null)";
        db.execSQL(img);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}