package com.example.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM DbObject")
    List<DbObject> index();

    //@Query("SELECT * FROM DbObject WHERE id = :id")
    //DbObject get(int id);

    @Insert
    void insert(DbObject... posts);
    @Update
    void update(DbObject... posts);

    @Delete
    void delete(DbObject... posts);

    @Query("DELETE FROM DbObject")
    void deleteAll();

}
