package com.example.hiwin.teacher_version_bob;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main4Activity extends AppCompatActivity {
    /*
        reference:
            https://developer.android.com/guide/topics/connectivity/bluetooth.html

     */
    private ListView listView;
    private final String strs[] = {"Vocabulary", "Vocabulary exam", "Conversation", "Home"};
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent it=getIntent();
        String address=it.getStringExtra("address");
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        device=bluetoothAdapter.getRemoteDevice(address);


        listView = (ListView) findViewById(R.id.ActionList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListView);


    }

    /***
     * 點擊ListView事件Method
     */
    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {

                ParcelUuid[] uuids = device.getUuids();
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                socket.connect();
                OutputStream outputStream = socket.getOutputStream();
                InputStream inStream = socket.getInputStream();
                outputStream.write(new byte[]{1,2,3,4,6,5});
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(Main4Activity.this, "點選第 " + (position + 1) + " 個 \n內容：" + strs[position], Toast.LENGTH_SHORT).show();
        }
    };




}
