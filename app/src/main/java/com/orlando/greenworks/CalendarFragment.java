package com.orlando.greenworks;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Locale;


public class CalendarFragment extends Fragment {
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout scheduleList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        MainActivity.fragmentTitle.setText(R.string.calendar); // Set the title of the fragment in the toolbar.

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        scheduleList = view.findViewById(R.id.scheduleList);

        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Clear the layout
            scheduleList.removeAllViews();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    showSchedule("Monday, " + monthName + " " +dayOfMonth, "Garbage");
                    break;
                case Calendar.THURSDAY:
                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Recycling");
                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Yard Waste");
                    break;
                default:
                    Log.i("Day of week", "Invalid day");
            }

        });



        return view;
    }

    void showSchedule(String date, String recyclingType) {
        // Inflate the layout for schedule design
        View scheduleView = inflater.inflate(R.layout.schedule_design, container, false);

        // Find the views in the layout
        CardView scheduleCard = scheduleView.findViewById(R.id.scheduleCardView);
        TextView scheduleDate = scheduleView.findViewById(R.id.dateTV);
        ImageView scheduleRecyclingIcon = scheduleView.findViewById(R.id.scheduleIV);
        TextView scheduleRecyclingType = scheduleView.findViewById(R.id.recyclingTypeTV);
        TextView scheduleTime = scheduleView.findViewById(R.id.timeTV);

        // Set the properties of the views with the custom information
        scheduleDate.setText(date);
        scheduleRecyclingType.setText(recyclingType);
        scheduleTime.setText("6:00 AM");

        switch (recyclingType) {
            case "Garbage":
                scheduleRecyclingIcon.setImageResource(R.drawable.garbage);
                scheduleCard.setBackgroundTintList(getResources().getColorStateList(R.color.medium_dark_green));
                break;
            case "Recycling":
                scheduleRecyclingIcon.setImageResource(R.drawable.recycling);
                scheduleCard.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                break;
            case "Yard Waste":
                scheduleRecyclingIcon.setImageResource(R.drawable.yard_waste);
                scheduleCard.setBackgroundTintList(getResources().getColorStateList(R.color.dark_green));
                break;

                // TODO: add default case show nothing is schedule for that day
        }

//        scheduleRecyclingIcon.setImageResource(recyclingIcon);

        // Add the schedule view to the scheduleList
        scheduleList.addView(scheduleView);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.
    }
}




