package com.example.assignment1;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.provider.Event;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterEvent extends RecyclerView.Adapter<RecyclerAdapterEvent.EventViewHolder> {

    public class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView eventIdTextView, eventNameTextView, catIdTextView, ticketsAvailabeTextView, isActiveTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventIdTextView = itemView.findViewById(R.id.eventIdTvLayout);
            eventNameTextView = itemView.findViewById(R.id.eventNameTvLayout);
            catIdTextView = itemView.findViewById(R.id.catIdTvLayout_event);
            ticketsAvailabeTextView = itemView.findViewById(R.id.ticketsEventTvLayout);
            isActiveTextView = itemView.findViewById(R.id.isEventActiveTvLayout);
        }
    }

    List<Event> eventArrayList = new ArrayList<>();

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_event, parent, false); //CardView inflated as RecyclerView list item
        EventViewHolder viewHolder = new EventViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.eventIdTextView.setText(eventArrayList.get(position).getEventId());
        holder.eventNameTextView.setText(eventArrayList.get(position).getEventName());
        holder.catIdTextView.setText(eventArrayList.get(position).getCategoryId());
        holder.ticketsAvailabeTextView.setText(String.valueOf(eventArrayList.get(position).getTicketsAvailable()));
        if (eventArrayList.get(position).isActive()) {
            holder.isActiveTextView.setText("Active");
            holder.itemView.setBackgroundResource(android.R.color.holo_green_dark);
        } else {
            holder.isActiveTextView.setText("Inactive");
            holder.itemView.setBackgroundResource(android.R.color.holo_red_dark);
        }

        View eventView = holder.itemView;

        eventView.setOnClickListener(v -> {
            String selectedEventName = eventArrayList.get(position).getEventName();

            Context context = eventView.getContext();
            Intent intent = new Intent(context, EventGoogleResult.class);
            intent.putExtra("eventName", selectedEventName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (this.eventArrayList != null) {
            return this.eventArrayList.size();
        }
        return 0;
    }

    public void setData(List<Event> data) {
        this.eventArrayList = data;
    }
}
