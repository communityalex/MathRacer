package com.community.zenith.mathracer.studentsactivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.UtilManager;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.community.zenith.mathracer.raceactivity.RaceActivity;
import com.community.zenith.mathracer.studentsactivity.StudentsAdapter;
import com.example.alex.mathracer.backend.mathRacerApi.model.Question;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class StudentsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.new_student_fab) FloatingActionButton newStudentFab;
    @BindView(R.id.students_recycler_view) RecyclerView studentsRecyclerView;
    @BindView(R.id.students_empty_layout) View studentsEmptyView;
    @BindView(R.id.students_view) View studentsView;
    @BindView(R.id.students_progress) View studentsProgress;

    @BindView(R.id.student_view) View studentView;
    @BindView(R.id.student_name) TextView studentName;
    @BindView(R.id.student_identifier) TextView studentIdentifier;
    @BindView(R.id.student_graph) GraphView studentGraph;
    @BindView(R.id.races_recycler_view) RecyclerView raceRecyclerView;
    @BindView(R.id.student_progress) View studentProgress;
    @BindView(R.id.student_loaded_content) View loadedContent;
    @BindView(R.id.races_empty_view) View racesEmptyView;
    @BindView(R.id.login_as_student) View loginAsStudent;

    @BindView(R.id.race_view) View raceView;
    @BindView(R.id.questions_recycler_view) RecyclerView questionRecyclerView;
    @BindView(R.id.race_title) TextView raceTitle;
    @BindView(R.id.race_time) TextView raceTime;

    @BindView(R.id.new_student_view) View newStudentView;
    @BindView(R.id.save_student_fab) FloatingActionButton saveStudentFab;

    @BindView(R.id.new_student_first_name) EditText newFirstName;
    @BindView(R.id.new_student_last_name) EditText newLastName;

    private StudentsAdapter studentsAdapter;
    private RacesAdapter raceAdapter;
    private QuestionsAdapter questionsAdapter;
    private Current status = Current.STUDENTS;

    private Teacher currentTeacher;
    private Student currentStudent;
    private Race currentRace;

    private List<Student> students;
    private List<Race> races;
    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        ButterKnife.bind(this);
        newStudentFab.setOnClickListener(this);
        saveStudentFab.setOnClickListener(this);
        loginAsStudent.setOnClickListener(this);
        currentTeacher = unpackageObject("Teacher", Teacher.class);
        studentsProgress.setVisibility(View.VISIBLE);
        studentsRecyclerView.setVisibility(View.GONE);
        new RxMaker<List<Student>>()
                .make(new RxRequest(RxType.GET_STUDENTS).longs(currentTeacher.getId()))
                .subscribe(new Subscriber<List<Student>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Students", "completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final List<Student> students) {
                        StudentsActivity.this.students = students;
                        if (students != null){
                            loadStudentsAdapter();
                        } else {
                            studentsEmptyView.setVisibility(View.VISIBLE);
                            studentsRecyclerView.setVisibility(View.GONE);
                        }
                        studentsProgress.setVisibility(View.GONE);
                    }
                });
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

    private void showStudentView(Student s) {
        status = Current.STUDENT;
        currentStudent = s;
        studentView.setVisibility(View.VISIBLE);
        studentsView.setVisibility(View.GONE);
        studentName.setText(s.getFirstName() + " " + s.getLastName());
        studentIdentifier.setText(s.getIdentifier());
        if (races == null){
            studentProgress.setVisibility(View.VISIBLE);
            new RxMaker<List<Race>>()
                    .make(new RxRequest(RxType.GET_RACES).longs(currentTeacher.getId(), currentStudent.getId()))
                    .subscribe(new Subscriber<List<Race>>() {
                        @Override
                        public void onCompleted() {}

                        @Override
                        public void onError(Throwable e) {}

                        @Override
                        public void onNext(final List<Race> races) {
                            studentProgress.setVisibility(View.GONE);
                            if (races != null){
                                StudentsActivity.this.races = races;
                                loadedContent.setVisibility(View.VISIBLE);
                                studentGraph.addSeries(new LineGraphSeries<>(getDataPoints(races)));
                                raceRecyclerView.setLayoutManager(new LinearLayoutManager(StudentsActivity.this));
                                raceAdapter = new RacesAdapter(races, StudentsActivity.this);
                                raceAdapter.setOnItemClickListener(new RacesAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {
                                        showRaceView(races.get(position));
                                    }

                                    @Override
                                    public void onItemLongClick(int position, View v) {

                                    }
                                });
                                raceRecyclerView.setAdapter(raceAdapter);
                            } else {
                                racesEmptyView.setVisibility(View.VISIBLE);
                                loadedContent.setVisibility(View.GONE);
                            }
                        }
                    });
        }


    }

    private void showRaceView(Race race) {
        currentRace = race;
        status = Current.RACE;
        raceTitle.setText("(" + race.getNumCorrect() + "/" + race.getNumQuestions() + ")");
        raceTime.setText(UtilManager.timeSince(race.getStartTime()));
        if (questions == null){
            new RxMaker<ArrayList<Question>>()
                    .make(new RxRequest(RxType.GET_QUESTIONS).longs(currentTeacher.getId(), currentStudent.getId(), currentRace.getId()))
                    .subscribe(new Subscriber<ArrayList<Question>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ArrayList<Question> questions) {
                            if (questions != null){
                                questionRecyclerView.setLayoutManager(new LinearLayoutManager(StudentsActivity.this));
                                questionsAdapter = new QuestionsAdapter(questions, StudentsActivity.this);
                                questionsAdapter.setOnItemClickListener(new QuestionsAdapter.ClickListener() {
                                    @Override
                                    public void onItemClick(int position, View v) {

                                    }

                                    @Override
                                    public void onItemLongClick(int position, View v) {

                                    }
                                });
                                questionRecyclerView.setAdapter(questionsAdapter);
                            }
                        }
                    });
        }

    }

    private DataPoint [] getDataPoints(List<Race> races){
        DataPoint [] points = new DataPoint[races.size()];
        for (int i = 0; i < races.size(); i++){
            int perc = (int) ((double)races.get(i).getNumCorrect() / (double) races.get(i).getNumQuestions()) * 100;
            points[i] = new DataPoint(i, perc);
        }
        return points;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_student_fab:
                showNewStudentView();
                break;
            case R.id.save_student_fab:
                createStudent();
                try {
                    startActivity(new Intent(this, StudentsActivity.class).putExtra("Teacher", new ObjectMapper().writeValueAsString(currentTeacher)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.login_as_student:
                try {
                    startActivity(new Intent(this, RaceActivity.class)
                            .putExtra("Student", new ObjectMapper().writeValueAsString(currentStudent))
                            .putExtra("Teacher", new ObjectMapper().writeValueAsString(currentTeacher)));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void createStudent() {
        Student s = new Student();
        s.setFirstName(newFirstName.getText().toString().trim());
        s.setLastName(newLastName.getText().toString().trim());
        s.setIdentifier(newFirstName.getText().toString().trim() + new Random().nextInt(101));

        new RxMaker<Student>()
                .make(new RxRequest(RxType.INSERT_STUDENT).longs(currentTeacher.getId()).object(s))
                .subscribe(new Subscriber<Student>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Student student) {
                        Log.d("Students", student.toString());
                        if (students == null){
                            students = new ArrayList<>();
                            studentsEmptyView.setVisibility(View.GONE);
                            studentsRecyclerView.setVisibility(View.VISIBLE);
                            studentsView.setVisibility(View.VISIBLE);
                            newStudentView.setVisibility(View.GONE);
                        }
                        studentsProgress.setVisibility(View.GONE);
                        students.add(student);
                        loadStudentsAdapter();
                    }
                });

    }

    private void loadStudentsAdapter() {
        if (studentsAdapter != null){
            studentsAdapter.swap(students);
        } else {
            studentsRecyclerView.setLayoutManager(new LinearLayoutManager(StudentsActivity.this));
            studentsAdapter = new StudentsAdapter(students, StudentsActivity.this);
            studentsAdapter.setOnItemClickListener(new StudentsAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    showStudentView(students.get(position));
                }

                @Override
                public void onItemLongClick(int position, View v) {

                }
            });
            studentsRecyclerView.setAdapter(studentsAdapter);
        }
        studentsRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNewStudentView() {
        newStudentView.setVisibility(View.VISIBLE);
        studentsView.setVisibility(View.GONE);
        status = Current.NEW_STUDENT;
    }

    @Override
    public void onBackPressed(){
        switch (status){
            case STUDENTS:
                break;
            case STUDENT:
                showStudentsView();
                break;
            case RACE:
                showStudentView(currentStudent);
                break;
            case NEW_STUDENT:
                showStudentsView();
                break;
        }
    }

    private void showStudentsView() {
        races = null;

        studentsRecyclerView.setVisibility(View.VISIBLE);
        studentsView.setVisibility(View.VISIBLE);
        studentView.setVisibility(View.GONE);
        raceView.setVisibility(View.GONE);
        newStudentView.setVisibility(View.GONE);
        status = Current.STUDENTS;
    }

    private enum Current {
        STUDENTS, STUDENT, RACE, NEW_STUDENT
    }
}
