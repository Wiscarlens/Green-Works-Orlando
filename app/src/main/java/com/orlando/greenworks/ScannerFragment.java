package com.orlando.greenworks;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * This is a collaborative effort by the following team members:
 * Team members:
 * - Wiscarlens Lucius (Team Leader)
 * - Amanpreet Singh
 * - Alexandra Perez
 * - Eric Klausner
 * - Jordan Kinlocke
 * */

public class ScannerFragment extends BottomSheetDialogFragment {
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    public ScannerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        NotificationHelper.createNotificationChannel(getContext());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        barcodeView = view.findViewById(R.id.barcodeScannerView);
        ImageButton typeBarcode = view.findViewById(R.id.typeBarcode);
        ImageButton guessItem = view.findViewById(R.id.guessItem);
        ImageButton flashlight = view.findViewById(R.id.flashLight);

        AtomicBoolean isFlashOn = new AtomicBoolean(false);

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(requireActivity().getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(requireActivity());

        barcodeView.setStatusText("");

        backButton.setOnClickListener(v -> {
            dismiss(); // Close the bottom sheet
        });

        typeBarcode.setOnClickListener(v -> {
            // Create an AlertDialog.Builder instance
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

            // Create an EditText instance
            EditText input = new EditText(requireActivity());

            // Set the EditText instance as the AlertDialog's view
            builder.setView(input);

            // Set the AlertDialog's message
            builder.setMessage("Please enter the barcode");

            // Set the AlertDialog's positive button
            builder.setPositiveButton("OK", (dialog, which) -> {
                String barcode = input.getText().toString();
                // Handle the entered barcode
                Toast.makeText(requireActivity(), "Entered barcode: " + barcode, Toast.LENGTH_SHORT).show();
            });

            // Set the AlertDialog's negative button
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            // Show the AlertDialog
            builder.show();
        });

        guessItem.setOnClickListener(v -> {
            // Open the item guess dialog
            Toast.makeText(requireActivity(), "Opening item guess...", Toast.LENGTH_SHORT).show();
        });

        flashlight.setOnClickListener(v -> {
            if(!isFlashOn.get()){
                barcodeView.setTorchOn();
                flashlight.setImageResource(R.drawable.baseline_flash_off_24);

                isFlashOn.set(true);
            } else {
                barcodeView.setTorchOff();
                flashlight.setImageResource(R.drawable.baseline_flash_on_24);

                isFlashOn.set(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestCameraPermission();
        } else {
            // Permission has already been granted
            barcodeView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
//    }

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();


            // TODO: Add logic to handle the scanned barcode
            if (result.getText().equals("078742040370")) {
                Drawable image = getResources().getDrawable(R.drawable.water_bottle, null);

                showItemDialog(image, "Water Bottle", "This is a water bottle");
            } else {
                Toast.makeText(requireActivity(), result.getText() + " Not found", Toast.LENGTH_SHORT).show();
            }



            beepManager.playBeepSoundAndVibrate();

        }



        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }


    };

    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();

        if (dialog != null) {
            ViewGroup bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
                behavior.setHideable(true);

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    void showItemDialog(Drawable image, String name, String description) {
        RelativeLayout relativeLayout = requireActivity().findViewById(R.id.fragment_scanner);
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.item_dialog, relativeLayout);

        ImageView itemImage = view.findViewById(R.id.itemImageIV);
        TextView itemName = view.findViewById(R.id.itemNameTV);
        TextView itemDescription = view.findViewById(R.id.itemDescriptionTV);
        ImageButton openButton = view.findViewById(R.id.openButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        itemImage.setImageDrawable(image);
        itemName.setText(name);
        itemDescription.setText(description);

        openButton.setOnClickListener(v -> {
            // Open the item in the browser
            dialog.dismiss();

            Toast.makeText(requireActivity(), "Opening item...", Toast.LENGTH_SHORT).show();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);

            // Convert 20dp to pixels and subtract it from the screen height
            params.y = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        }

        dialog.show();

    }
    private void requestCameraPermission() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Camera Permission Needed")
                    .setMessage("This app needs the Camera permission, please accept to use camera functionality")
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                CAMERA_PERMISSION_REQUEST_CODE);
                    })
                    .create()
                    .show();
        } else {
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, yay! Do the
                // camera-related task you need to do.
                barcodeView.resume();
            } else {
                // Permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(requireActivity(), "Permission denied to camera", Toast.LENGTH_SHORT).show();
            }
            return;

            // Other 'case' lines to check for other
            // permissions this app might request.
        }
    }



}