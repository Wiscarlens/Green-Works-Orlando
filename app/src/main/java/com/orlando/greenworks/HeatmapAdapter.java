package com.orlando.greenworks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

class HeatmapAdapter extends RecyclerView.Adapter<HeatmapViewHolder>
{
    private final ArrayList<EventDay> daysOfMonth;
    private final ArrayList<EventDay> greenDays;
    private final OnItemListener onItemListener;

    public HeatmapAdapter(ArrayList<EventDay> daysOfMonth, ArrayList<EventDay> greenDays, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.greenDays = greenDays;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public HeatmapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.heatmap_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);


        return new HeatmapViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HeatmapViewHolder holder, int position)
    {

        int gray = ContextCompat.getColor(holder.calendarCellLL.getContext(), R.color.gray);
        int lightGreen = ContextCompat.getColor(holder.calendarCellLL.getContext(), R.color.light_green);
        int green = ContextCompat.getColor(holder.calendarCellLL.getContext(), R.color.green);
        int mediumDarkGreen = ContextCompat.getColor(holder.calendarCellLL.getContext(), R.color.medium_dark_green);
        int darkGreen = ContextCompat.getColor(holder.calendarCellLL.getContext(), R.color.dark_green);

        for (int i = 0; i < greenDays.size(); i++)
        {
            if (daysOfMonth.get(position).getDay() == greenDays.get(i).getDay() && daysOfMonth.get(position).getMonth() == greenDays.get(i).getMonth())
            {
                if (greenDays.get(i).getFrequency() == 1) {
                    holder.calendarCellLL.setBackgroundColor(lightGreen);
                } else if (greenDays.get(i).getFrequency() == 2) {
                    holder.calendarCellLL.setBackgroundColor(green);
                } else if (greenDays.get(i).getFrequency() == 3) {
                    holder.calendarCellLL.setBackgroundColor(mediumDarkGreen);
                }  else if (greenDays.get(i).getFrequency() > 3) {
                    holder.calendarCellLL.setBackgroundColor(darkGreen);
                } else if (greenDays.get(i).getFrequency() < 0) {
                    holder.calendarCellLL.setBackgroundColor(gray);
                }
            }
        }

    }

    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
