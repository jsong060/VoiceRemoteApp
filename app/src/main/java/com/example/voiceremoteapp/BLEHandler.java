package com.example.voiceremoteapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BLEHandler {
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;

    //UUID of connected central device for testing
    private UUID connectedDeviceUUID = UUID.fromString("5309565c-0798-4951-af9b-2d2d171d2cff");
    protected HashMap<UUID, String> connectedDeviceList = new HashMap<UUID, String>();

    private static final long SCAN_PERIOD = 10000; // to stop scanning after 10 seconds

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            // things to do with the device
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            //super.onBatchScanResults(results);

        }

        @Override
        public void onScanFailed(int errorCode) {
            //super.onScanFailed(errorCode);
        }
    };



}
