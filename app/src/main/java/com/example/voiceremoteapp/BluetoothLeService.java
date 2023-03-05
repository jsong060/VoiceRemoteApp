package com.example.voiceremoteapp;



import static android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class BluetoothLeService extends Service{

    public static final String TAG = "BluetoothLeService";
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning = false;

    private UUID connectedDeviceUUID = UUID.fromString("5309565c-0798-4951-af9b-2d2d171d2cff");
    protected HashMap<UUID, String> deviceList = new HashMap<UUID, String>();

    ArrayList<BluetoothDevice> devicesDiscovered = new ArrayList<BluetoothDevice>();

    private static final long SCAN_PERIOD = 10000; // to stop scanning after 10 seconds



    private static final int REQUEST_ENABLE_BT = 1;
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;



    //connection state operation variables
    private int connectionState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    private Handler handler =new Handler();




    private Binder binder = new LocalBinder();

    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        String intentAction;
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //successfully connected to the GATT server
                intentAction = ACTION_GATT_CONNECTED;
                connectionState = STATE_CONNECTED;
                //insert broadcastupdate
                Log.i(TAG, "Connected to GATT server!");
                //insert discovery attempt


            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //disconnected from the GATT server
                intentAction = ACTION_GATT_DISCONNECTED;
                connectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server!");
                //insert broadcast update
            }
        }

        //insert onservicesdiscovered

        //insert oncharacteristicread


    };

    // define broadcastupdate(final string)

    // define broadcastupdate( final string, final BluetoothGattCharacteristic, final Intent)

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    public boolean initialize() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a Bluetooth adapter.");
            return false;
        }
        return true;
    }

    public boolean connect(final String address) {
        if(bluetoothAdapter == null || address == null) {
            Log.w(TAG, "Bluetooth adapter not initialized or null address!");
            return false;
        }

        try {
            final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
        } catch (IllegalArgumentException i) {
            Log.w(TAG, "Unable to find device with provided address!");
            return false;
        }

        // connect to the GATT server on the device
        return false;
    }

    @SuppressLint("NewApi")
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
