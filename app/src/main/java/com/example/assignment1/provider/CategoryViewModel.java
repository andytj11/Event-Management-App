package com.example.assignment1.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    /*
    ViewModel class is used for pre-processing the data,
    before passing it to the controllers (Activity or Fragments).
     */
    private CategoryRepository repository;
    private LiveData<List<Category>> allCategoryLiveData;

    public CategoryViewModel(@NonNull Application application){
        super(application);
        repository = new CategoryRepository(application);
        allCategoryLiveData = repository.getAllCategory();
    }

    // crud operations
    // create, read, update, delete

    // Create
    /**
     * ViewModel method to insert one single category,
     * usually calling insert method defined in repository class
     * @param category object containing details of new category to be inserted
     */
    public void insert(Category category) {
        repository.insert(category);
    }

    // Read
    /**
     * ViewModel method to get all categories
     * @return LiveData of type List<Category>
     */
    public LiveData<List<Category>> getAllCategory() {
        return allCategoryLiveData;
    }

    // Update
    /**
     * ViewModel method to increment event count of category id
     * usually calling insert method defined in repository class
     */
    public void incrementEventCount(String categoryId){
        repository.incrementEventCount(categoryId);
    }

    // Delete
    /**
     * ViewModel method to delete all category
     * usually calling insert method defined in repository class
     */
    public void deleteAllCategory() {
        repository.deleteAll();
    }

}
