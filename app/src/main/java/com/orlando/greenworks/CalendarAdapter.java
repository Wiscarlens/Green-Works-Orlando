package com.orlando.greenworks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

/**
 * The CalendarAdapter class extends the RecyclerView.Adapter class.
 * It represents an adapter for a RecyclerView to display a list of days in a month.
 */
class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>
{
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    /**
     * This constructor initializes the list of days and the item click listener.
     * @param daysOfMonth The list of days in a month.
     * @param onItemListener The listener for item clicks.
     */
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener)
    {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    /**
     * This method creates a new ViewHolder for the RecyclerView.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);

        return new CalendarViewHolder(view, onItemListener);
    }

    /**
     * This method binds the data to the ViewHolder.
     * It sets the day of the month and the visibility of the dots based on the position.
     * @param holder The ViewHolder to be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position)
    {
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        if (dayText.isEmpty()) {
            holder.itemView.setClickable(false);

            holder.greenDot.setVisibility(View.GONE);
            holder.darkGreenDot.setVisibility(View.GONE);
            holder.mediumGreenDot.setVisibility(View.GONE);
        } else {
            holder.itemView.setClickable(true);

            if (position % 7 == 1) { // Monday
                holder.mediumGreenDot.setVisibility(View.VISIBLE);
            } else {
                holder.greenDot.setVisibility(View.GONE);
            }
            if (position % 7 == 4) { // Thursday
                holder.greenDot.setVisibility(View.VISIBLE);
                holder.darkGreenDot.setVisibility(View.VISIBLE);
            }


        }
    }

    /**
     * This method returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount()
    {
        return daysOfMonth.size();
    }

    /**
     * This interface defines the method for responding to item clicks.
     */
    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
