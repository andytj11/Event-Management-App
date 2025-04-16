package com.example.assignment1;

import static androidx.recyclerview.widget.RecyclerView.*;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment1.provider.Category;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapterCategory extends RecyclerView.Adapter<RecyclerAdapterCategory.CategoryViewHolder> {

    public class CategoryViewHolder extends ViewHolder {

        public TextView catIdTextView, catNameTextView, eventCountTextView, isActiveTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            catIdTextView = itemView.findViewById(R.id.catIdTvLayout_cat);
            catNameTextView = itemView.findViewById(R.id.catNameTvLayout);
            eventCountTextView = itemView.findViewById(R.id.eventCountTvLayout);
            isActiveTextView = itemView.findViewById(R.id.isCatActiveTvLayout);
        }
    }

    public List<Category> categoryArrayList = new ArrayList<>();
    private final int HEADER_CARD_TYPE = 0;
    private final int VALUE_CARD_TYPE = 1;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == HEADER_CARD_TYPE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_category_header, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_category, parent, false);
        }
        CategoryViewHolder viewHolder = new CategoryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        if (position != 0) {
            holder.catIdTextView.setText(categoryArrayList.get(position - 1).getCategoryId());
            holder.catNameTextView.setText(categoryArrayList.get(position - 1).getCategoryName());
            holder.eventCountTextView.setText(String.valueOf(categoryArrayList.get(position - 1).getEventCount()));
            holder.isActiveTextView.setText(String.valueOf(categoryArrayList.get(position - 1).isActive()));
            if (categoryArrayList.get(position - 1).isActive()){
                holder.isActiveTextView.setText("Yes");
            } else {
                holder.isActiveTextView.setText("No");
            }

            View categoryView = holder.itemView;

            categoryView.setOnClickListener(v -> {
                String selectedCategoryLocation = categoryArrayList.get(position-1).getCategoryLocation();

                Context context = categoryView.getContext();
                Intent intent = new Intent(context, GoogleMapActivity.class);
                intent.putExtra("categoryLocation", selectedCategoryLocation);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (categoryArrayList != null) {
            return categoryArrayList.size() + 1;
        }
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return HEADER_CARD_TYPE;
        else return VALUE_CARD_TYPE;
    }

    public void setData(List<Category> data) {
        categoryArrayList = data;
    }
}
