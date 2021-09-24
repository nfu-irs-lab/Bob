package com.example.hiwin.teacher_version_bob.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.hiwin.teacher_version_bob.DeviceAdapter;
import com.example.hiwin.teacher_version_bob.R;
import com.example.hiwin.teacher_version_bob.fragment.ModeDialogFragment;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothConnectionActivity extends AppCompatActivity {
    /*
        reference:
            http://tw.gitbook.net/android/android_bluetooth.html
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.devs_toolbar);
        setSupportActionBar(myToolbar);

        ListView deviceList = (ListView) findViewById(R.id.devicesList);

        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, 0);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceList.setAdapter(getDeviceAdaptor(bluetoothAdapter.getBondedDevices()));
        deviceList.setOnItemClickListener(onClickListView);
    }

    private DeviceAdapter getDeviceAdaptor(Set<BluetoothDevice> bondedDevices) {
        return new DeviceAdapter(this, new ArrayList<>(bondedDevices));
    }

    private final AdapterView.OnItemClickListener onClickListView = (parent, view, position, id) -> {
        BluetoothDevice selected_device = (BluetoothDevice) parent.getItemAtPosition(position);
        Toast.makeText(BluetoothConnectionActivity.this, selected_device.getName(), Toast.LENGTH_SHORT).show();

        ModeDialogFragment newFragment = new ModeDialogFragment();
        newFragment.setListener(new ModeDialogFragment.OptionListener() {
            @Override
            public void select(ModeDialogFragment.Mode mode) {
                Intent it=null;
                if (mode == ModeDialogFragment.Mode.face_detect)
                    it = new Intent(BluetoothConnectionActivity.this, FaceDetectActivity.class);
                else if (mode == ModeDialogFragment.Mode.object_detect)
                    it = new Intent(BluetoothConnectionActivity.this, ObjectDetectActivity.class);
                else
                    throw new RuntimeException("unknown mode.");
                it.putExtra("address", selected_device.getAddress());
                startActivity(it);

            }
        });
        newFragment.show(getSupportFragmentManager(), "missiles");
    };
}
