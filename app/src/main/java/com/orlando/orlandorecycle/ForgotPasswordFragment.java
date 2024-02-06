package com.orlando.orlandorecycle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;


public class ForgotPasswordFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Get the GridLayout from the fragment_forgot_password layout
        GridLayout heatmapGrid = view.findViewById(R.id.heatmapGrid);

        // Example data - replace this with your actual data
        int[][] heatmapData = {
                {1, 2, 3, 4, 5, 6, 7},
                {8, 9, 10, 11, 12, 13, 14},
                // ... continue with your data
        };

        // Populate the heatmap dynamically
        for (int[] heatmapDatum : heatmapData) {
            for (int i : heatmapDatum) {
                TextView cell = new TextView(requireContext());
                cell.setLayoutParams(new GridLayout.LayoutParams());
                cell.setText(String.valueOf(i)); // Set your data here
                cell.setGravity(View.TEXT_ALIGNMENT_CENTER);
                cell.setBackgroundResource(getColorForValue(i)); // Customize based on your data

                heatmapGrid.addView(cell);
            }
        }

        return view;
    }

    private int getColorForValue(int value) {
        // Implement your logic to assign colors based on data value
        // Example: return Color.RED for high values, Color.GREEN for low values
        // You can use more sophisticated logic based on your use case
        return requireContext().getColor(R.color.dark_bleu); // R.color.your_heatmap_color should be defined in your resources
    }
}