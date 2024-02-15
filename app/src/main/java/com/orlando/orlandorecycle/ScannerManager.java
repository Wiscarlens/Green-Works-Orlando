package com.orlando.orlandorecycle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScannerManager {
    private final ActivityResultLauncher<ScanOptions> scannerLauncher;
    private String scanItem = null;

    private final ScanCompleteListener listener;

    public ScannerManager(MainActivity mainActivity, ScanCompleteListener listener) {
        this.listener = listener;
        // Initialize the scannerLauncher using the provided fragment
        scannerLauncher = mainActivity.registerForActivityResult(
                new ScanContract(), result -> {
                    scanItem = result.getContents();
                    listener.onScanComplete(scanItem);
                }
        );
    }

//    public ScannerManager(MainActivity mainActivity) {
//        // Initialize the scannerLauncher using the provided fragment
//        scannerLauncher = mainActivity.registerForActivityResult(
//                new ScanContract(), result -> scanItem = result.getContents()
//        );
//    }

    public String getScanItem() {
        return scanItem;
    }

    public void startBarcodeScanning() {
        ScanOptions options = new ScanOptions();

        options.setPrompt("Press Volume Up to Turn Flash On");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        scannerLauncher.launch(options);
    }

    public static class CaptureAct extends CaptureActivity {
    }

    public interface ScanCompleteListener {
        void onScanComplete(String result);
    }
}


