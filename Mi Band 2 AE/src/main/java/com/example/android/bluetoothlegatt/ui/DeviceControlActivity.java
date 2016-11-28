/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt.ui;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.android.bluetoothlegatt.R;
import com.example.android.bluetoothlegatt.reuse.event.ClickEvent;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private TextView mConnectionState;
    private TextView mDataField;
    private String mDeviceName;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private ToggleButton toggleButton;
    public Handler handler;

    // Manual CRUDN Button lists
    private Button aeRegistration;
    private Button cntRegistration;
    private Button cinRegistration;

    private Button aeRetrieve;
    private Button cntRetrieve;
    private Button cinRetrieve;

    private Button aeDelete;
    private Button cntDelete;
    private Button cinDelete;

    // Device IP Address
    private TextView tvDeviceIPaddress;

    // Walking step value
    public static String walkingStepValue = "0";

    public DeviceControlActivity() {
        handler = new Handler();
    }

    /* Response callback Interface */
    public interface IReceived {
        void getResponseBody(String msg);
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void startMonitoring() {
        if (mGattCharacteristics != null) {
            int groupPosition = -1;
            int childPosition = 0;

            int serviceCount = mGattCharacteristics.size();

            for(int i=0; i < serviceCount; i++) {
                ArrayList<BluetoothGattCharacteristic> s = mGattCharacteristics.get(i);

                for(int j=0; j < s.size(); j++) {
                    BluetoothGattCharacteristic c = s.get(j);
                    UUID uuid = c.getUuid();

                    if (uuid.compareTo(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_GUID)) == 0) {
                        Log.i(TAG, "Found characteristic to monitor");
                        groupPosition = i;
                        childPosition = j;
                        break;
                    }
                }
            }

            if (groupPosition == -1)
                return;

            final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(groupPosition).get(childPosition);
            final int charaProp = characteristic.getProperties();

            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

                // If there is an active notification on a characteristic, clear
                // it first so it doesn't update the data field on the user interface.
                if (mNotifyCharacteristic != null) {
                    mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, false);
                    mNotifyCharacteristic = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);
            }

            if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                mNotifyCharacteristic = characteristic;
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
        }
    }


    private void clearUI() {
        //mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mDataField.setText(R.string.no_data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        //mGattServicesList = (ExpandableListView) findViewById(R.id.gatt_services_list);

        mConnectionState = (TextView) findViewById(R.id.connection_state);
        mDataField = (TextView) findViewById(R.id.data_value);

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        toggleButton = (ToggleButton) findViewById(R.id.toggleBtn);

       // Button lists
       aeRegistration = (Button)findViewById(R.id.ae_registration);
       cntRegistration = (Button)findViewById(R.id.cnt_registration);
       cinRegistration = (Button)findViewById(R.id.cin_registration);

       aeRetrieve = (Button)findViewById(R.id.ae_retrieve);
       cntRetrieve = (Button)findViewById(R.id.cnt_retrieve);
       cinRetrieve = (Button)findViewById(R.id.cin_retrieve);

        aeDelete = (Button)findViewById(R.id.ae_delete);
        cntDelete = (Button)findViewById(R.id.cnt_delete);
        cinDelete = (Button)findViewById(R.id.cin_delete);

        // Network Listener Setting
        ClickEvent clickEvent = new ClickEvent(getApplicationContext(), XMLResponseListener, JSONResponseListener,
                aeRegistration, cntRegistration, cinRegistration,
                aeRetrieve, cntRetrieve, cinRetrieve,
                aeDelete, cntDelete, cinDelete);

        clickEvent.setListener( );

        toggleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(((ToggleButton) view).isChecked()){
                    aeRegistration.setVisibility(View.INVISIBLE);
                    cntRegistration.setVisibility(View.INVISIBLE);
                    cinRegistration.setVisibility(View.INVISIBLE);

                    aeRetrieve.setVisibility(View.INVISIBLE);
                    cntRetrieve.setVisibility(View.INVISIBLE);
                    cinRetrieve.setVisibility(View.INVISIBLE);

                    aeDelete.setVisibility(View.INVISIBLE);
                    cntDelete.setVisibility(View.INVISIBLE);
                    cinDelete.setVisibility(View.INVISIBLE);

                } else {
                    aeRegistration.setVisibility(View.VISIBLE);
                    cntRegistration.setVisibility(View.VISIBLE);
                    cinRegistration.setVisibility(View.VISIBLE);

                    aeRetrieve.setVisibility(View.VISIBLE);
                    cntRetrieve.setVisibility(View.VISIBLE);
                    cinRetrieve.setVisibility(View.VISIBLE);

                    aeDelete.setVisibility(View.VISIBLE);
                    cntDelete.setVisibility(View.VISIBLE);
                    cinDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    HttpRequester.NetworkResponseListenerXML XMLResponseListener = new HttpRequester.NetworkResponseListenerXML() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, HttpRequester.NetworkResponseListenerXML networkResponseListenerXML, String responseBody) {
            Log.i("testing", "XML onSuccess");
            Log.i("testing", ""+ statusCode);

        }


        @Override
        public void onFail(int statusCode, Header[] headers, HttpRequester.NetworkResponseListenerXML networkResponseListenerXML, String responseBody) {
            Log.i("testing", "XML onFail");
            Log.i("testing", ""+ statusCode);

        }
    };

    HttpRequester.NetworkResponseListenerJSON JSONResponseListener = new HttpRequester.NetworkResponseListenerJSON() {

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
            Log.i("testing", "JSON onSuccess");

            if(jsonObject != null) {
                Log.i("testing", jsonObject.toString());

            }

        }

        @Override
        public void onFail(int statusCode, Header[] headers, JSONObject jsonObject, String responseString) {
            Log.i("testing", "JSON onFail");

            if(jsonObject != null) {
                Log.i("testing", jsonObject.toString());

            } else if(responseString != null) {
                Log.i("testing", responseString);

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        tvDeviceIPaddress = (TextView)findViewById(R.id.tv_device_ipaddress);
        tvDeviceIPaddress.setText(getLocalIpAddress()+":62590");
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddr = inetAddress.getHostAddress();
                        return ipAddr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("Server", ex.toString());
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);

        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case R.id.menu_monitor:
                startMonitoring();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        Log.i("testing", "Data Value : " + data);
        if (data != null) {
            String hex = data.split("\n")[1];
            String[] hex1 = hex.split(" ");
            Log.i(TAG, " display data: " + data );
            String hexValue = hex1[2].concat(hex1[1]);
            Integer i = Integer.parseInt(hexValue,16);
            Log.i(TAG, " display data: " + hexValue + " " + i);
            mDataField.setText(""+ i);
            walkingStepValue = "" + i;

            //ContentInstance contentInstanceRegistration = new ContentInstance(getApplicationContext(), XMLResponseListener, JSONResponseListener, new ContentInstance_Create());
            //contentInstanceRegistration.oneM2MResuest();
        }
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;

        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);

        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();

            uuid = gattService.getUuid().toString();

            currentServiceData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();

                uuid = gattCharacteristic.getUuid().toString();

                currentCharaData.put(LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }

            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public Context getContext() {
        return getApplicationContext();
    }

    public HttpRequester.NetworkResponseListenerXML getXMLResponseListener() {
        return XMLResponseListener;
    }

    public HttpRequester.NetworkResponseListenerJSON getJSONResponseListener() {
        return JSONResponseListener;
    }
}