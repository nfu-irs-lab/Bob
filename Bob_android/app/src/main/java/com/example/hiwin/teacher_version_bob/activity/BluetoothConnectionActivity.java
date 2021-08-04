package com.example.hiwin.teacher_version_bob.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.DeviceAdapter;
import com.example.hiwin.teacher_version_bob.R;

import java.util.ArrayList;

public class BluetoothConnectionActivity extends AppCompatActivity {
    /*
        reference:
            http://tw.gitbook.net/android/android_bluetooth.html

     */

    private final ArrayList<BluetoothDevice> bondedDevices = new ArrayList<>();
    private ListView deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.devs_toolbar);
        setSupportActionBar(myToolbar);
        deviceList = (ListView) findViewById(R.id.devicesList);
//        Request For opening bluetooth
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);
        updateView();
    }

    private void updateView() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bondedDevices.addAll(bluetoothAdapter.getBondedDevices());
        DeviceAdapter da = new DeviceAdapter(this, bondedDevices);
        deviceList.setAdapter(da);
        deviceList.setOnItemClickListener(onClickListView);
    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice selected_device = (BluetoothDevice) parent.getItemAtPosition(position);
            Toast.makeText(BluetoothConnectionActivity.this, selected_device.getName(), Toast.LENGTH_SHORT).show();

//            Intent it = new Intent(BluetoothConnectionActivity.this, BluetoothTerminalActivity.class);
            Intent it = new Intent(BluetoothConnectionActivity.this, MainActivity.class);
            it.putExtra("address", selected_device.getAddress());
            startActivity(it);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }
}
