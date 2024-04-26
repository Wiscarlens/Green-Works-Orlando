package com.orlando.greenworks.view.fragments;

import static com.orlando.greenworks.view.utils.DialogUtils.makeDialogFullscreen;

import android.Manifest;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.orlando.greenworks.R;
import com.orlando.greenworks.model.Item;
import com.orlando.greenworks.view.utils.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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

    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        barcodeView = view.findViewById(R.id.barcodeScannerView);

        LinearLayout editTextLayout = view.findViewById(R.id.editTextLayout);
        EditText barcodeEditText = view.findViewById(R.id.barcodeET);
        Button doneButton = view.findViewById(R.id.doneButton);
        LinearLayout bottomButtonsLayout = view.findViewById(R.id.bottomButtonsLayout);

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
            editTextLayout.setVisibility(View.VISIBLE);
            bottomButtonsLayout.setVisibility(View.GONE);

            barcodeView.pause();

            doneButton.setOnClickListener(v1 -> {
                barcodeView.resume();
                String barcode = barcodeEditText.getText().toString();

                if (barcode.isEmpty()) {
                    editTextLayout.setVisibility(View.GONE);
                    bottomButtonsLayout.setVisibility(View.VISIBLE);

                    Toast.makeText(requireActivity(), "Please enter a barcode", Toast.LENGTH_SHORT).show();
                } else {
                    if (!findBarcode(barcode)) {
                        Toast.makeText(requireActivity(), barcode + " Not found", Toast.LENGTH_SHORT).show();
                    } else {
                        beepManager.playBeepSoundAndVibrate();
                    }

                    editTextLayout.setVisibility(View.GONE);
                    bottomButtonsLayout.setVisibility(View.VISIBLE);
                }
            });
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

    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();

            // TODO: Set last text value back to null after to second

            // logic to handle the scanned barcode
            if (!findBarcode(result.getText())) {
                Toast.makeText(requireActivity(), result.getText() + " Not found", Toast.LENGTH_SHORT).show();
            } else {
                beepManager.playBeepSoundAndVibrate();
            }

        }



        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }


    };

    private boolean findBarcode(String result) {
        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("Purified Water",
                "To recycle a water water bottle, first empty any remaining water, remove the cap, and rinse the bottle to remove residue. Check for the recycling symbol, typically found on the bottle's bottom. Dasani bottles are usually made of PET plastic, which is recyclable. Place the rinsed bottle in your recycling bin following your local recycling guidelines for plastic bottles.",
                10, "purified_water", "078742040370"));
        items.add(new Item("Lysol",
                "Empty the Container: Make sure the container is completely empty. Use up all the product until there's nothing left inside.\n" +
                        "Remove the Nozzle: Remove the plastic spray nozzle if it's detachable. This helps in ensuring that the container is properly emptied.\n" +
                        "Check for Recycling Symbols: Look for recycling symbols on the container. Some Lysol spray bottles are made of recyclable materials, but it's essential to check the label or the bottom of the container for recycling instructions and symbols.\n" +
                        "Rinse the Container: Rinse the container thoroughly with water to remove any residue. This step helps in preparing the container for recycling and prevents contamination of other recyclable materials.\n" +
                        "Check with Local Recycling Guidelines: Different municipalities have different recycling guidelines. Check with your local recycling center or municipality's website to find out if they accept Lysol spray bottles for recycling and how they should be prepared.g",
                5, "lysol", "036241748289"));

        for (Item item : items) {
            if (item.getBarcode().equals(result)) {
                showItemDialog(item);

                barcodeView.pause();

                return true;
            }
        }

        return false;
    }

    public void onStart() {
        super.onStart();

        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        makeDialogFullscreen(dialog);

    }

    void showItemDialog(Item item) {
        RelativeLayout relativeLayout = requireActivity().findViewById(R.id.fragment_scanner);
        View view = LayoutInflater.from(requireActivity()).inflate(R.layout.item_dialog, relativeLayout);

        ImageView itemImage = view.findViewById(R.id.itemImageIV);
        TextView itemName = view.findViewById(R.id.itemNameTV);
        TextView itemDescription = view.findViewById(R.id.itemDescriptionTV);
        ImageButton openButton = view.findViewById(R.id.openButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        Drawable image = ImageUtils.getDrawableFromName(requireContext(), item.getItemImagePath());

        itemImage.setImageDrawable(image);
        itemName.setText(item.getItemName());
        itemDescription.setText(item.getItemDescription());

        openButton.setOnClickListener(v -> {
            dialog.dismiss();
            barcodeView.resume();

            // Create a new instance of ItemInformationFragment with the item
            ItemInformationFragment itemInformationFragment = ItemInformationFragment.newInstance(item, false);

            // Show the fragment
            itemInformationFragment.show(((AppCompatActivity) requireContext())
                    .getSupportFragmentManager(), itemInformationFragment.getTag());

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
}