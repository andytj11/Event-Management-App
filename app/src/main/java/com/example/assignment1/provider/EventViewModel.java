package com.example.assignment1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EventViewModel extends AndroidViewModel {

    private EventRepository repository;
    private LiveData<List<Event>> allEventLiveData;

    public EventViewModel(@NonNull Application application){
        super(application);
        repository = new EventRepository(application);
        allEventLiveData = repository.getAllEvent();
    }

    // crud operations
    // create, read, update, delete

    // Create
    /**
     * ViewModel method to insert one single event,
     * usually calling insert method defined in repository class
     * @param event object containing details of new event to be inserted
     */
    public void insert(Event event) {
        repository.insert(event);
    }

    // Read
    /**
     * ViewModel method to get all events
     * @return LiveData of type List<Event>
     */
    public LiveData<List<Event>> getAllEvent() {
        return allEventLiveData;
    }

    // Delete - delete all events
    /**
     * ViewModel method delete all events
     */
    public void deleteAllEvent(){
        repository.deleteAll();
    }

    // Delete - delete newest events/undo
    /**
     * ViewModel method delete newest events
     */
    public void deleteNewestEvent() {
        repository.deleteNewestEvent();
    }

}
