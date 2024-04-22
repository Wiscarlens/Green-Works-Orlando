package com.orlando.greenworks.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orlando.greenworks.R;

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
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    /**
     * This constructor initializes the list of days and the item click listener.
     * @param daysOfMonth The list of days in a month.
     * @param onItemListener The listener for item clicks.
     */
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
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
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
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
    public interface  OnItemListener {
        void onItemClick(int position, String dayText);
    }

    /**
     * The CalendarViewHolder class extends the RecyclerView.ViewHolder class.
     * It represents a view holder for a day in the calendar.
     * The view holder includes a text view for the day of the month, a background circle view, and three dot views for different types of recycling.
     */
    public static class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView dayOfMonth;
        private final View backgroundCircle;
        final View greenDot;
        final View darkGreenDot;
        final View mediumGreenDot;
        private final CalendarAdapter.OnItemListener onItemListener;
        private static View previousView = null;

        /**
         * The constructor for the CalendarViewHolder class.
         * @param itemView The view for the day in the calendar.
         * @param onItemListener The listener for the item.
         */
        public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            backgroundCircle = itemView.findViewById(R.id.backgroundCircle);
            greenDot = itemView.findViewById(R.id.greenDot);
            darkGreenDot = itemView.findViewById(R.id.blueDot);
            mediumGreenDot = itemView.findViewById(R.id.yellowDot);

            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        /**
         * The selectDay method selects the day in the calendar.
         */
        public void selectDay() {
            if(previousView != null){
                previousView.findViewById(R.id.backgroundCircle).setVisibility(View.INVISIBLE);
                ((TextView) previousView.findViewById(R.id.cellDayText)).setTextColor(Color.BLACK);
            }
            backgroundCircle.setVisibility(View.VISIBLE);
            dayOfMonth.setTextColor(Color.BLACK);
        }

        /**
         * The currentDay method highlights the current day in the calendar.
         */
        public void currentDay() {
            backgroundCircle.setVisibility(View.VISIBLE);
            backgroundCircle.setBackgroundResource(R.drawable.current_day_circle);
            dayOfMonth.setTextColor(Color.BLACK);
        }

        /**
         * The onClick method is called when the day in the calendar is clicked.
         * @param view The view for the day in the calendar.
         */
        @Override
        public void onClick(View view) {
            selectDay();
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
            previousView = view;
        }
    }
}
