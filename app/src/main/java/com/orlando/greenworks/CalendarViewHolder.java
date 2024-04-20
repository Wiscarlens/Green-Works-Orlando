package com.orlando.greenworks;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The CalendarViewHolder class extends the RecyclerView.ViewHolder class.
 * It represents a view holder for a day in the calendar.
 * The view holder includes a text view for the day of the month, a background circle view, and three dot views for different types of recycling.
 */
public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

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
    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener)
    {
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
    public void onClick(View view)
    {
        selectDay();
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
        previousView = view;
    }
}