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

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout scheduleList;
    TextView noSchedule;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;

        MainActivity.fragmentTitle.setText(R.string.calendar); // Set the title of the fragment in the toolbar.

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);

        scheduleList = view.findViewById(R.id.scheduleList);
        noSchedule = view.findViewById(R.id.noScheduleTV);

        ImageView previousMonth = view.findViewById(R.id.previousMonthBtn);
        ImageView nextMonth = view.findViewById(R.id.nextMonthBtn);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();
        }

        setMonthView();

        previousMonth.setOnClickListener(this::previousMonthAction);
        nextMonth.setOnClickListener(this::nextMonthAction);

//        CalendarView calendarView = view.findViewById(R.id.calendarView);
//
//        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
//            scheduleList.removeAllViews();  // Clear the layout before adding new schedule
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(year, month, dayOfMonth);
//
//            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//            String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
//
//            switch (dayOfWeek) {
//                case Calendar.MONDAY:
//                    showSchedule("Monday, " + monthName + " " +dayOfMonth, "Garbage");
//                    break;
//                case Calendar.THURSDAY:
//                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Recycling");
//                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Yard Waste");
//                    break;
//                case Calendar.TUESDAY:
//                case Calendar.WEDNESDAY:
//                case Calendar.FRIDAY:
//                case Calendar.SATURDAY:
//                case Calendar.SUNDAY:
//                    noSchedule.setVisibility(View.VISIBLE);
//                    scheduleList.addView(noSchedule); // Add the noSchedule TextView back to the layout
//                    break;
//                default:
//                    Log.i("Day of week", "Invalid day");
//            }
//
//        });

      Switch switchPickupNotifications = view.findViewById(R.id.switch_pickup_notifications);
switchPickupNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
    if (isChecked) {
        scheduleNotifications();
        Toast.makeText(getActivity(), "You have enabled trash pickup notifications", Toast.LENGTH_SHORT).show();
    } else {
        cancelNotifications();
        Toast.makeText(getActivity(), "You have disabled trash pickup notifications", Toast.LENGTH_SHORT).show();
    }
});

        return view;
    }


    private void scheduleNotifications() {
    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getActivity(), NotificationReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

    // Schedule the alarm to start at approximately 2:00 p.m.
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 14);

    // With setInexactRepeating(), you have to use one of the AlarmManager interval
    // constants--in this case, AlarmManager.INTERVAL_DAY.
    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY, pendingIntent);
}

private void cancelNotifications() {
    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getActivity(), NotificationReceiver.class);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

    alarmManager.cancel(pendingIntent);
}
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            yearMonth = YearMonth.from(date);
        }

        int daysInMonth = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            daysInMonth = yearMonth.lengthOfMonth();
        }

        LocalDate firstOfMonth = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            firstOfMonth = selectedDate.withDayOfMonth(1);
        }
        int dayOfWeek = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        }

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.format(formatter);
        }
        return null;
    }

    public void previousMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.minusMonths(1);
        }
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.plusMonths(1);
        }
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.isEmpty())
        {
            scheduleList.removeAllViews();  // Clear the layout before adding new schedule

            // Calculate the clicked date
            LocalDate clickedDate;

            int year = 0;
            int month = 0;
            int dayOfMonth = 0;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                clickedDate = selectedDate.withDayOfMonth(1).plusDays(position - selectedDate.withDayOfMonth(1).getDayOfWeek().getValue());
                year = clickedDate.getYear();
                month = clickedDate.getMonthValue();
                dayOfMonth = clickedDate.getDayOfMonth();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, dayOfMonth); // Note: Calendar class expects month value to be 0-based

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    showSchedule("Monday, " + monthName + " " + dayOfMonth, "Garbage");
                    break;
                case Calendar.THURSDAY:
                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Recycling");
                    showSchedule("Thursday, " + monthName + " " + dayOfMonth, "Yard Waste");
                    break;
                case Calendar.TUESDAY:
                case Calendar.WEDNESDAY:
                case Calendar.FRIDAY:
                case Calendar.SATURDAY:
                case Calendar.SUNDAY:
                    noSchedule.setVisibility(View.VISIBLE);
                    scheduleList.addView(noSchedule); // Add the noSchedule TextView back to the layout
                    break;
                default:
                    Log.i("Day of week", "Invalid day");
            }
        }
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

        noSchedule.setVisibility(View.GONE);

        // Set the properties of the views with the custom information
        scheduleDate.setText(date);
        scheduleRecyclingType.setText(recyclingType);
        scheduleTime.setText(R.string.default_collection_time); // TODO: Pick up time add to admin settings

        switch (recyclingType) {
            case "Garbage":
                scheduleRecyclingIcon.setImageResource(R.drawable.garbage);
                scheduleCard.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.medium_dark_green));
                break;
            case "Recycling":
                scheduleRecyclingIcon.setImageResource(R.drawable.recycling);
                scheduleCard.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));
                break;
            case "Yard Waste":
                scheduleRecyclingIcon.setImageResource(R.drawable.yard_waste);
                scheduleCard.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.dark_green));
                break;

                // TODO: add default case show nothing is schedule for that day
        }


        // Add the schedule view to the scheduleList
        scheduleList.addView(scheduleView);
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.
    }
}




