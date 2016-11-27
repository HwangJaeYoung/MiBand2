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

package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import com.example.android.bluetoothlegatt.testing.oneM2MTester;
/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity
{

    private final static String TAG = DeviceScanActivity.class.getSimpleName();

    private LeDeviceListAdapter mLeDeviceListAdapter; //list of BLE devices we found

    private BluetoothAdapter mBluetoothAdapter; // this is we are getting from the system to start scanning

    private boolean mScanning;

    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private TextView mBLEFoundTextView;

    private TextView mDevicesFoundTextView;

    private oneM2MTester tester;



    @Override
    public void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

// Setting up our labels
        mBLEFoundTextView = (TextView) findViewById(R.id.intro_message_view);
        mDevicesFoundTextView = (TextView) findViewById(R.id.message2_view);


        mBLEFoundTextView.setText("Looking for Mi Band 2...");

        mDevicesFoundTextView.setText("");

        getActionBar().setTitle(R.string.title_devices);

        // Setting up our labels *****************************
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter(); // Getting the adapter for bluetooth

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

         tester = new oneM2MTester(getApplicationContext());

         try {
             tester.getWebServer().start();
         } catch(IOException ioe) {
             Log.w("Httpd", "The server could not start.");
         }

         Log.w("Httpd", "Web server initialized.");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning)
        {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        }
        else
        {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                mBLEFoundTextView.setText("Looking for Mi Band 2...");
                mDevicesFoundTextView.setText("No devices detected");

                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case R.id.menu_connect2:
                Log.i(TAG, "Connect to BLE Peripheral");
                int position = -1;
                for(int i=0; i < mLeDeviceListAdapter.getCount(); i++)
                {
                    BluetoothDevice d = mLeDeviceListAdapter.getDevice(i);
                    Log.i(TAG,"device name is: '" + d.getName() + "'");

                    if (d.getName() != null &&
                            d.getName().compareTo("MI Band 2") == 0)
                    {
                        Log.i(TAG, "Got device");
                        position = i;
                        break;
                    }
                }
                if (position != -1)
                {
                    final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
                    if (device == null)
                        return (false);
                    final Intent intent = new Intent(this, DeviceControlActivity.class);
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
                    intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                    if (mScanning)
                    {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }
                    startActivity(intent);
                }
                else
                    Log.i(TAG, "Unable to find device");

        }
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled())
        {
            if (!mBluetoothAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED)
        {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable)
    {
        if (enable)
        {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else
        {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter
    {

        private ArrayList<BluetoothDevice> mLeDevices;

        private LayoutInflater mInflator;

        public LeDeviceListAdapter()
        {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device)
        {
            if (!mLeDevices.contains(device))
            {
                if (device.getName() == null)
                    return;

                mLeDevices.add(device);
                //String devCount = Integer.toString(mLeDevices.size()) + " devices found" + device.toString();
                String devCount = "Mi band 2 found: " + device.toString();
                mDevicesFoundTextView.setText(devCount);
                Log.i(TAG, "===================================");
                Log.i(TAG, "======>>>>> Adding device: " + (device.getName() == null ? "Unknown" : device.getName()));
                Log.i(TAG, "===================================");

                if (device.getName().compareTo("MI Band 2") == 0)
                    mBLEFoundTextView.setText("Mi Band 2 found!");

            }
        }

        public BluetoothDevice getDevice(int position)
        {
            return mLeDevices.get(position);
        }

        public void clear()
        {
            mLeDevices.clear();
        }

        @Override
        public int getCount()
        {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i)
        {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null)
            {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback()
            {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    @Override
    public void onDestroy()  {
        super.onDestroy();

        /**** Testing code ****/
        // DON'T FORGET to stop the server
        if (tester.getWebServer() != null)
            tester.getWebServer().stop();
    }
}