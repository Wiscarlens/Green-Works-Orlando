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
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.CalendarView;
import android.widget.Toast;
import com.orlando.greenworks.R;


public class CalendarFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        MainActivity.fragmentTitle.setText(R.string.calendar); // Set the title of the fragment in the toolbar.

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Handle the date change
                String msg = "Selected date: " + dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.

    }
}




