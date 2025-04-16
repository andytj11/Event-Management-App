package com.example.assignment1.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDAO {

    // crud operations
    // create, read, update, delete

    // Create
    @Insert
    void insert(Event event);

    // Read
    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAllEvent();

    // Delete - delete All
    @Query("DELETE FROM events")
    void deleteAllEvent();

    // Delete - delete the newest event/undo
    @Query("DELETE FROM events WHERE id = (SELECT MAX(id) FROM events)")
    void deleteNewestEvent();

}
