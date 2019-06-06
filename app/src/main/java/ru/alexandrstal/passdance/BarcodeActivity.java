package ru.alexandrstal.passdance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.*;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {

    public static final String EXTRA_QR_RESULT = "EXTRA_QR_RESULT";

    private BarcodeDetector mBarcodeDetector;
    private CameraSource mCameraSource;
    private CameraSourcePreview mPreview;

    private static final String TAG = "BarcodeActivity";
    private static final int PERMISSIONS_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_barcode);

        mPreview = findViewById(R.id.cameraSourcePreview);

        if (isPermissionGranted()) {
            setupBarcodeDetector();
            setupCameraSource();
        } else {
            requestPermission();
        }

    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();

            } else {
                Toast.makeText(this, "Приложению требуется разрешение для работы с камерой для чтения QR-кодов", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setupBarcodeDetector() {
        mBarcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();
                if (barCodes.size() != 0) {
                    String data = barCodes.valueAt(0).displayValue;
                    Log.d(TAG, "Barcode detected: " + data);
                    returnData(data);
                }
            }
        });

        if (!mBarcodeDetector.isOperational())
            Log.w(TAG, "Detector dependencies are not yet available.");
    }

    private void setupCameraSource() {
        mCameraSource = new CameraSource.Builder(getApplicationContext(), mBarcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();
    }

    private void startCameraSource() {
        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void playBeep() {
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    private void returnData(String data) {
        if (validateQR(data)) {
            playBeep();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_QR_RESULT, data);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPreview != null) {
            mPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    private boolean validateQR(String date) {
        if (date == null) {
            return false;
        }
        return date.split(" ").length >= 3;
    }
}
