package com.example.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.finalproject.items.logInSave;

@Database(entities = {DbObject.class, logInSave.class}, version = 1)
@TypeConverters(DbObjectConverter.class) // Apply the converter to the database
public abstract class AppDB extends RoomDatabase {
    public abstract PostDao postDao();
    public abstract LogInSaveDao logInSaveDao();
}
