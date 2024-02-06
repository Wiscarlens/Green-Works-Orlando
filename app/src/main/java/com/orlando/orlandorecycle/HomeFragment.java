package com.orlando.orlandorecycle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageButton sortingGuideButton = view.findViewById(R.id.sorting_guideIB);

        sortingGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, forgotPasswordFragment).addToBackStack(null).commit();
            }
        });

        return view;
    }
}