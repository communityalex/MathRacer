package com.community.zenith.mathracer.raceactivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.community.zenith.mathracer.GenericPagerAdapter;
import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.community.zenith.mathracer.google.SlidingTabLayoutWhite;
import com.community.zenith.mathracer.loginactivity.LoginFragment;
import com.community.zenith.mathracer.loginactivity.SignUpFragment;
import com.community.zenith.mathracer.studentsactivity.RacesAdapter;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class RaceActivity extends AppCompatActivity {

    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.tabs) SlidingTabLayoutWhite tabs;

    private RacesAdapter racesAdapter;
    private Student currentStudent;
    private Teacher currentTeacher;
    final private int REQUEST_COARSE_LOCATION_PERMISSIONS = 10;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    BluetoothSocket socket;
    OutputStream outputStream;
    private final String DEVICE_ADDRESS = "00:6A:8E:16:C7:B1";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Serial Port Service ID'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);
        ButterKnife.bind(this);

        currentTeacher = unpackageObject("Teacher", Teacher.class);
        currentStudent = unpackageObject("Student", Student.class);

        doDiscovery();

        tabs.setCustomTabView(R.layout.custom_tab, 0);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayoutWhite.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.green) ;   //define any color in xml resources and set it here, I have used white
            }

        });
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        pager.setAdapter(new GenericPagerAdapter(getSupportFragmentManager(), this)
                .titles("Race", "Drive", "Stats")
                .fragments(RaceFragment.class, DriveFragment.class, StatsFragment.class));
        pager.setOffscreenPageLimit(2);
        tabs.setViewPager(pager);
    }

    public void doDiscovery() {
        int hasPermission = ActivityCompat.checkSelfPermission(RaceActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            loadBluetooth();
            return;
        }
        ActivityCompat.requestPermissions(RaceActivity.this,
                new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_COARSE_LOCATION_PERMISSIONS);
    }

    public <T> T unpackageObject(String text, Class<T> type){
        if (getIntent().getStringExtra(text) != null){
            try {
                return new ObjectMapper().readValue(getIntent().getStringExtra(text), type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String [] permissions, int [] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION_PERMISSIONS: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadBluetooth();
                } else {
                    Toast.makeText(this,
                            getResources().getString(R.string.permission_failure),
                            Toast.LENGTH_LONG)
                            .show();
                }
                break;
            }
        }
    }

    private void loadBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                if (iterator.getAddress().equals(DEVICE_ADDRESS)){ //Replace with iterator.getName() if comparing Device names.
                    device = iterator; //device is an object of type BluetoothDevice
                    Toast.makeText(getApplicationContext(), "Paired.", Toast.LENGTH_SHORT).show();
                    break;

                }
            }
        }
        if (device != null){
            try {
                socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
                try {
                    socket.connect();
                } catch (Exception ex){
                    Class<?> clazz = socket.getRemoteDevice().getClass();
                    Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};

                    Method m = null;
                    try {
                        m = clazz.getMethod("createRfcommSocket", paramTypes);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    Object[] params = new Object[] {Integer.valueOf(1)};

                    try {
                        socket = (BluetoothSocket) m.invoke(socket.getRemoteDevice(), params);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(broadcastReceiver);
    }

    public Student getStudent(){
        return currentStudent;
    }

    public Teacher getTeacher(){
        return currentTeacher;
    }

    public void sendBluetooth(char x) {
        if (outputStream != null){
            try {
                Log.d("Bluetooth", "Sending " + x);
                outputStream.write((byte)x);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

