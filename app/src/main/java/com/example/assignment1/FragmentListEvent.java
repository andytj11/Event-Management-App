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

import com.example.assignment1.provider.Event;
import com.example.assignment1.provider.EventViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FragmentListEvent extends Fragment {

    public List<Event> listEvents = new ArrayList<>();
    public RecyclerAdapterEvent recyclerAdapterEvent;
    public RecyclerView recyclerViewEvent;
    private EventViewModel eventViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapterEvent = new RecyclerAdapterEvent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_list_event, container, false);

        recyclerViewEvent = fragmentView.findViewById(R.id.recyclerViewEvent);
        recyclerViewEvent.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewEvent.setAdapter(recyclerAdapterEvent);

        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);

        eventViewModel.getAllEvent().observe(getViewLifecycleOwner(), newData -> {
            recyclerAdapterEvent.setData(newData);
            recyclerAdapterEvent.notifyDataSetChanged();
        });
        return fragmentView;
    }


}