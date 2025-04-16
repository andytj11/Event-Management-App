package com.example.assignment1.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CategoryRepository {

    // private class variable to hold reference to categoryDAO
    private CategoryDAO categoryDAO;

    // private class variable to temporary hold all the categories retrieved and pass outside of this class
    private LiveData<List<Category>> allCategoryLiveData;

    CategoryRepository(Application application){
        EMADatabase db = EMADatabase.getDatabase(application);

        categoryDAO = db.categoryDAO();
        allCategoryLiveData = categoryDAO.getAllCategory();
    }

    // crud operations
    // create, read, update, delete

    // Create
    /**
     * Repository method to insert one single category
     * @param category object containing details of new category to be inserted
     */
    void insert(Category category) {
        EMADatabase.databaseWriteExecutor.execute(() -> categoryDAO.insert(category));
    }

    // Read
    /**
     * Repository method to get all categories
     * @return LiveData of type List<Category>
     */
    LiveData<List<Category>> getAllCategory() {
        return allCategoryLiveData;
    }

    // Update
    /**
     * Repository method to increment event count by 1 for category id
     */
    void incrementEventCount(String categoryId){
        EMADatabase.databaseWriteExecutor.execute(() -> categoryDAO.incrementEventCount(categoryId));
    }

    // Delete
    /**
     * Repository method to delete all category
     */
    void deleteAll(){
        EMADatabase.databaseWriteExecutor.execute(() -> categoryDAO.deleteAll());
    }

}
