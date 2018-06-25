package com.example.julhygabriel.diary.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.julhygabriel.diary.R;
import com.example.julhygabriel.diary.model.Note;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<Note> dataList;
    private Context context;

    public CustomAdapter(Context context,List<Note> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle;
        TextView txtContent;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            txtContent = mView.findViewById(R.id.content);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.txtTitle.setText(dataList.get(position).getTitle());
        String label = getColor(dataList.get(position).getLabel());
        int color = Color.parseColor(label);
        holder.txtTitle.setBackgroundColor(color);
        holder.txtContent.setText(dataList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public String getColor(String label) {
        switch (label){
            case "red":
                return "#FF0000";
            case "blue":
                return "#0000FF";
            case "green":
                return "#00FF00";
            case "grey":
                return "#EEEEEE";
            default:
                return "#FFFFFF";
        }
    }
}