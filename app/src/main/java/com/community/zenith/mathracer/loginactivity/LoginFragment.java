package com.community.zenith.mathracer.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.studentsactivity.StudentsActivity;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class LoginFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.login_identifier) EditText identifier;
    @BindView(R.id.login_password) EditText password;
    @BindView(R.id.login_button) View loginButton;
    @BindView(R.id.login_progress) View loginProgress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void attemptLogin(){
        identifier.setVisibility(View.GONE);
        password.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        String id = identifier.getText().toString().trim();
        String pass = password.getText().toString().trim();

        new RxMaker<Teacher>()
                .make(new RxRequest(RxType.LOGIN_TEACHER).strings(id, pass))
                .subscribe(new Subscriber<Teacher>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Teacher teacher) {
                        if (teacher == null){
                            Toast.makeText(getActivity(), "Login failed.", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(new Intent(getActivity(), StudentsActivity.class)
                                .putExtra("Teacher", packageObject(teacher)));
                        }
                    }
                });
    }

    public String packageObject(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        loginButton.setOnClickListener(this);
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
            case R.id.login_button:
                attemptLogin();
                break;
        }
    }
}
