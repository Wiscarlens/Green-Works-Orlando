package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.orlando.greenworks.R;
import com.orlando.greenworks.view.utils.NotificationHelper;

import org.jetbrains.annotations.Nullable;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class TermsFragment extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationHelper.createNotificationChannel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton closeButton = view.findViewById(R.id.closeButton);
        TextView termsTextView = view.findViewById(R.id.termsText);
        termsTextView.setMovementMethod(new ScrollingMovementMethod());

        closeButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }
}