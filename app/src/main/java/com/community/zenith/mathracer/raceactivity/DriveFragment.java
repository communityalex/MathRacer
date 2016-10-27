package com.community.zenith.mathracer.raceactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.connection.BluetoothCodes;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriveFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.fab_forward) FloatingActionButton forward;
    @BindView(R.id.fab_backward) FloatingActionButton backward;
    @BindView(R.id.fab_right) FloatingActionButton right;
    @BindView(R.id.fab_left) FloatingActionButton left;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drive, container, false);
        ButterKnife.bind(this, view);
        forward.setOnClickListener(this);
        if (backward != null){
            backward.setOnClickListener(this);
        }
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_forward:
                getRaceActivity().sendBluetooth(BluetoothCodes.FORWARD);
                break;
            case R.id.fab_backward:
                getRaceActivity().sendBluetooth(BluetoothCodes.BACKWARD);
                break;
            case R.id.fab_right:
                getRaceActivity().sendBluetooth(BluetoothCodes.LEFT);
                break;
            case R.id.fab_left:
                getRaceActivity().sendBluetooth(BluetoothCodes.RIGHT);
                break;
        }
    }

    public Student getStudent(){
        return getRaceActivity().getStudent();
    }

    public RaceActivity getRaceActivity(){
        return ((RaceActivity) getActivity());
    }
}
