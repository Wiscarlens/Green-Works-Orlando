package com.orlando.greenworks.view.fragments;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orlando.greenworks.R;
import com.orlando.greenworks.view.MainActivity;
import com.orlando.greenworks.view.adapter.CalendarAdapter;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

/**
 * CalendarFragment is a Fragment that displays a calendar to the user.
 * It allows the user to navigate through the calendar and view schedules for each day.
 * It also provides the functionality to enable or disable notifications for trash pickup.
 */
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout scheduleList;
    TextView noSchedule;

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    /**
     * This method is called to do initial creation of the fragment.
     * It sets up the calendar view and the actions for the previous and next month buttons.
     * It also sets up the switch for enabling or disabling notifications.
     */
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

        return view;
    }



    /**
     * This method sets the month view for the calendar.
     * It displays the month and year at the top of the calendar.
     * It also sets up the calendar adapter to display the days of the month.
     */
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * This method creates an array of days in the month.
     * It calculates the number of days in the month and the day of the week for the first day of the month.
     * It then creates an array of days in the month with empty strings for days before and after the month.
     * @param date The selected date.
     * @return An array of days in the month.
     */
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

    /**
     * This method formats the month and year from the selected date.
     * @param date The selected date.
     * @return The formatted month and year.
     */
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

    /**
     * This method is called when the previous month button is clicked.
     * It decrements the selected date by one month and updates the month view.
     * @param view The previous month button.
     */
    public void previousMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.minusMonths(1);
        }
        setMonthView();
    }

    /**
     * This method is called when the next month button is clicked.
     * It increments the selected date by one month and updates the month view.
     * @param view The next month button.
     */
    public void nextMonthAction(View view)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = selectedDate.plusMonths(1);
        }
        setMonthView();
    }

    /**
     * This method is called when the day in the calendar is clicked.
     * It displays the schedule for the selected day.
     * @param position The position of the day in the calendar.
     * @param dayText The text of the day in the calendar.
     */
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

    /**
     * This method displays the schedule for the selected day.
     * @param date The date of the schedule.
     * @param recyclingType The type of recycling for the schedule.
     */
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

                scheduleCard.setOnClickListener(v -> showBottomSheetDialog(recyclingType,
                        "Garbage\n" +
                                "\n" +
                                "Place your garbage at the curb by 6 a.m. on your scheduled collection day.\n" +
                                "\n" +
                                "Garbage Regulations\n" +
                                "Garbage must be placed in carts provided by the City.\n" +
                                "Request one now.\n" +
                                "\n" +
                                "All household garbage should be bagged and placed inside the cart\n" +
                                "\n" +
                                " Do not overload the cart or place items on top of the cart\n" +
                                "\n" +
                                " For curb collection, carts should be placed at least two feet from obstacles, e.g. mailboxes, utility boxes, trees and shrubs\n" +
                                "\n" +
                                " Place the cart at the curb with the handle and the wheels facing the house\n" +
                                " Carts must be removed from the curb by the morning after your collection. Carts left on the curb the morning after the scheduled pickup may result in a $25 fee\n" +
                                " Do not place hazardous waste, such as rechargeable batteries, cleaning materials, oil, paint, pesticides or medical waste in the cart."));

                break;
            case "Recycling":
                scheduleRecyclingIcon.setImageResource(R.drawable.recycling);
                scheduleCard.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.green));

                scheduleCard.setOnClickListener(v -> showBottomSheetDialog(recyclingType,
                        "Recycling \n" +
                                "Put all of your recyclables into one cart. Your recycling is collected once per week. Place it on the curb prior to 6 a.m. on your pick up date\n" +
                                "\n" +
                                " Best Practices for Recycling\n" +
                                "\n" +
                                "The following guidelines will help us provide you with better service:\n" +
                                "\n" +
                                " - Place your recycle cart at the curb prior to 6 a.m. on your scheduled date of collection. Keep the cart at least two feet from obstacles such as mailboxes trees and shrubs, utility poles, meter boxes or parked cars.\n" +
                                " \n" +
                                "- Keep bags out of the cart by emptying your recycling directly into the cart. Bags and sheets of plastic tangle recycling equipment.\n" +
                                "\n" +
                                " -Do not overload the cart or place items on top of the cart.\n" +
                                " \n" +
                                "- Do not place garbage, yard waste or hazardous waste in the recycling cart.\n" +
                                " - Carts left at the curb on the morning after the scheduled pick up may result in a $25.00 roll back fee"));

                break;
            case "Yard Waste":
                scheduleRecyclingIcon.setImageResource(R.drawable.yard_waste);
                scheduleCard.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.dark_green));

                scheduleCard.setOnClickListener(v -> showBottomSheetDialog(recyclingType,
                        "Yard Waste\n" +
                                "\n" +
                                "Place it on the curb prior to 6 a.m. on your pick up date.\n" +
                                "\n" +
                                "\n" +
                                "Bag It, Don't Blow It!\n" +
                                "\n" +
                                "Yard waste blown into the street or piled up on curbs, or storm drains can cause major flooding and pollute our local waters\n" +
                                "\n" +
                                "Bag or bundle your yard waste for the city to collect and properly dispose of it.\n" +
                                "\n" +
                                "\n" +
                                "Things to Keep In Mind\n" +
                                "\n" +
                                "Yard waste includes only grass clippings, leaves, branches and twigs.\n" +
                                "\n" +
                                "Yard waste must be bagged or tied in bundles (each can only weigh up to 40 pounds each)\n" +
                                "\n" +
                                "Large branches can only be four inches in diameter and 40 pounds in weight Branches must also be bundled and tied.\n" +
                                "\n" +
                                " A maximum of four cubic yards collected each week. This is equivalent to what can fit in the bed of a standard pick-up truck\n" +
                                "\n" +
                                "\n" +
                                "Things to Avoid\n" +
                                "\n" +
                                " Yard waste does not include landscape ties, fences or landscape pressure treated wood.\n" +
                                "\n" +
                                " Bagged yard waste must not exceed 40 pounds each\n" +
                                "\n" +
                                " Large branches must not exceed four feet in length\n" +
                                "\n" +
                                " Yard waste that exceeds four cubic yards or weighs more than 40 pounds is considered a special pick up and may incur an extra charge. Any special pick up will incur a minimum $50 charge Yard waste that fills a claw truck will incur a $350 charge; half a claw truck will be a $175 charge, etc...\n" +
                                "\n" +
                                " Yard waste may not be placed inside the garbage cart. If yard waste is placed in the garbage cart, the cart will not be dumped until the yard waste is removed."));

                break;
        }


        // Add the schedule view to the scheduleList
        scheduleList.addView(scheduleView);
    }

    /**
     * This method shows a bottom sheet dialog with the recycling type and collection details.
     * @param recyclingType The type of recycling.
     * @param collectionDetails The collection details.
     */
    public void showBottomSheetDialog(String recyclingType, String collectionDetails) {
        final Dialog bottomSheetDialog = new Dialog(requireContext());

        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bottomSheetDialog.setContentView(R.layout.calendar_bottom_dialog);

        TextView recyclingTypeTV = bottomSheetDialog.findViewById(R.id.wasteType);
        TextView collectionDetailsTV = bottomSheetDialog.findViewById(R.id.collectionDetails);

        recyclingTypeTV.setText(recyclingType);
        collectionDetailsTV.setText(collectionDetails);

        bottomSheetDialog.show();

        Objects.requireNonNull(bottomSheetDialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * This method is called when the fragment is visible to the user and actively running.
     * It schedules notifications for trash pickup.
     */
    @Override
    public void onStop() {
        super.onStop();
        MainActivity.fragmentTitle.setText(""); // Clear the title of the fragment in the toolbar.
    }
}




