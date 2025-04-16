package com.example.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDAO {

    // crud operations
    // create, read, update, delete

    /*
    The Dao is an interface that defines the database operations that should be performed on the database.
     */

    // Create
    @Insert
    void insert(Category category);

    // Read
    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategory();

    // Update - increment event count by 1 for category ID
    @Query("UPDATE categories SET eventCount = eventCount + 1 WHERE categoryId = :categoryId")
    void incrementEventCount(String categoryId);

    // Delete
    @Query("DELETE FROM categories")
    void deleteAll();

}
