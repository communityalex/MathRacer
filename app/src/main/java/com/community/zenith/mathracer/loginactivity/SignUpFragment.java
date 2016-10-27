package com.community.zenith.mathracer.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.community.zenith.mathracer.studentsactivity.StudentsActivity;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class SignUpFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.sign_up_identifier) EditText identifier;
    @BindView(R.id.sign_up_password) EditText password;
    @BindView(R.id.sign_up_button) View signUpButton;
    @BindView(R.id.sign_up_progress) View signUpProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        signUpButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_button:
                attemptSignUp();
                break;
        }
    }

    private void attemptSignUp() {
        signUpProgress.setVisibility(View.VISIBLE);
        identifier.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        String id = identifier.getText().toString().trim();
        String pass = password.getText().toString().trim();
        Teacher t = new Teacher();
        t.setIdentifier(id);
        t.setPassword(pass);
        new RxMaker<Teacher>()
                .make(new RxRequest(RxType.INSERT_TEACHER).object(t))
                .subscribe(new Subscriber<Teacher>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Teacher teacher) {
                        try {
                            startActivity(new Intent(getActivity(), StudentsActivity.class)
                                .putExtra("Teacher", new ObjectMapper().writeValueAsString(teacher)));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
