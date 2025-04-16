package com.example.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class EventRepository {

    // private class variable to hold reference to EventDAO
    private EventDAO eventDAO;
    // private class variable to temporary hold all the events retrieved and pass outside of this class
    private LiveData<List<Event>> allEventLiveData;

    EventRepository(Application application){
        EMADatabase db = EMADatabase.getDatabase(application);

        eventDAO = db.eventDAO();
        allEventLiveData = eventDAO.getAllEvent();
    }

    // crud operations
    // create, read, update, delete

    // Create
    /**
     * Repository method to insert one single event
     * @param event object containing details of new category to be inserted
     */
    void insert(Event event) {
        EMADatabase.databaseWriteExecutor.execute(() -> eventDAO.insert(event));
    }

    // Read
    /**
     * Repository method to get all events
     * @return LiveData of type List<Category>
     */
    LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    // Delete - delete all
    /**
     * Repository method to delete all events
     */
    void deleteAll(){
        EMADatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteAllEvent());
    }

    // Delete - delete newest events/undo
    /**
     * Repository method to delete latest event
     */
    void deleteNewestEvent(){
        EMADatabase.databaseWriteExecutor.execute(() -> eventDAO.deleteNewestEvent());
    }

}
