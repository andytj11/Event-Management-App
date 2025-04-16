package com.example.assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment1.provider.Category;
import com.example.assignment1.provider.CategoryViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FragmentListCategory extends Fragment {

    public ArrayList<Category> listCategory = new ArrayList<>();
    public  RecyclerAdapterCategory recyclerAdapterCat;
    public RecyclerView recyclerViewCat;
    private CategoryViewModel categoryViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapterCat = new RecyclerAdapterCategory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list_category, container, false);
        recyclerViewCat = fragmentView.findViewById(R.id.recyclerViewCat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewCat.setLayoutManager(layoutManager);

        recyclerViewCat.setAdapter(recyclerAdapterCat);

        // Retrieve data from database and set it to recyclerview
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapterCat.setData(newData);
            recyclerAdapterCat.notifyDataSetChanged();
        });

        return fragmentView;
    }
}