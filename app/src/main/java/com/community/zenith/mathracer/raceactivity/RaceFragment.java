package com.community.zenith.mathracer.raceactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.UtilManager;
import com.community.zenith.mathracer.connection.BluetoothCodes;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.community.zenith.mathracer.studentsactivity.QuestionsAdapter;
import com.example.alex.mathracer.backend.mathRacerApi.model.Question;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class RaceFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.question_view) TextView question;
    @BindView(R.id.sub_question_view) TextView subQuestion;
    @BindView(R.id.answer_view) EditText answer;
    @BindView(R.id.send_view) View send;
    @BindView(R.id.questions_recycler_view) RecyclerView questionsRecyclerView;

    @BindView(R.id.race_complete_view) View raceCompleteView;
    @BindView(R.id.race_complete_text) TextView raceResult;
    @BindView(R.id.again_view) View againView;
    @BindView(R.id.races_empty_view) View raceEmptyView;

    private String currentQuestion;
    private int currentAnswer;
    private QuestionsAdapter questionsAdapter;
    private int numQuestions;
    private Race currentRace;
    private ArrayList<Question> questions = new ArrayList<>();
    private int currentNumQs = 0, correctQs;
    boolean restarting = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_race, container, false);
        ButterKnife.bind(this, view);
        send.setOnClickListener(this);
        againView.setOnClickListener(this);
        question.setText("Questions?");
        return view;
    }

    public Student getStudent(){
        return getRaceActivity().getStudent();
    }

    public Teacher getTeacher(){
        return getRaceActivity().getTeacher();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void sendAnswer(){
        String ans = answer.getText().toString().trim();
        Question q = new Question();
        q.setQuestion(currentQuestion);
        q.setCorrect(currentAnswer);
        if (!ans.isEmpty()){
            if (currentAnswer == Integer.parseInt(ans)){
                getRaceActivity().sendBluetooth(BluetoothCodes.CORRECT);
                subQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
                subQuestion.setText("CORRECT");
                correctQs++;
            } else {
                subQuestion.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                subQuestion.setText("INCORRECT");
                getRaceActivity().sendBluetooth(BluetoothCodes.INCORRECT);
            }
            if (currentNumQs < numQuestions - 1){
                generateRandomQuestion();
                currentNumQs++;
            } else {
                raceCompleteView.setVisibility(View.VISIBLE);
                question.setText("");
                answer.setText("");
                subQuestion.setText("");
                raceResult.setText(correctQs + "/" + numQuestions);
                correctQs = 0;
                currentNumQs = 0;
                restarting = true;
            }
            q.setSubmitted(Integer.parseInt(ans));
            new RxMaker<Question>()
                    .make(new RxRequest(RxType.ADD_QUESTION).longs(getTeacher().getId(), getStudent().getId(), currentRace.getId())
                            .object(q))
                    .subscribe(new Subscriber<Question>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Question question) {
                            questions.add(question);
                            loadAdapter();
                        }
                    });
            answer.setText("");
        }

    }

    private void loadAdapter() {
        if (questionsAdapter == null){
            questionsAdapter = new QuestionsAdapter(questions, getActivity());
            questionsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            questionsRecyclerView.setAdapter(questionsAdapter);
        } else {
            questionsAdapter.swap(questions);
        }
    }

    public void generateRandomQuestion(){
        int first = UtilManager.randomInRange(1, 9);
        int second = UtilManager.randomInRange(1, 9);
        switch (UtilManager.chooseRandom("+", "-", "/", "x")){
            case "+":
                currentQuestion = first + " + " + second;
                currentAnswer = first + second;
                break;
            case "-":
                if (first > second){
                    currentQuestion = first + " - " + second;
                    currentAnswer = first - second;
                } else {
                    generateRandomQuestion();
                    return;
                }
                break;
            case "/":
                if (first % second == 0){
                    currentQuestion = first + " / " + second;
                    currentAnswer = first / second;
                } else {
                    generateRandomQuestion();
                    return;
                }
                break;
            case "x":
                currentQuestion = first + " x " + second;
                currentAnswer = first * second;
                break;
        }
        question.setText(currentQuestion);
    }

    public RaceActivity getRaceActivity(){
        return ((RaceActivity) this.getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_view:
                if (currentQuestion == null){
                    if (!restarting){
                        startRace();
                    }
                    raceEmptyView.setVisibility(View.GONE);
                } else {
                    sendAnswer();
                }
                break;
            case R.id.again_view:
                raceCompleteView.setVisibility(View.GONE);
                question.setText("Question?");
                currentQuestion = null;
                restarting = false;
                questions = new ArrayList<>();
                questionsAdapter.swap(questions);
        }
    }

    private void startRace() {
        String nums = answer.getText().toString().trim();
        if (!nums.isEmpty()){
            answer.setText("");
            int n = Integer.parseInt(nums);
            getRaceActivity().sendBluetooth(BluetoothCodes.START);
            Race race = new Race();
            race.setNumQuestions(n);
            race.setNumCorrect(0);
            race.setStartTime(new Date().toString());
            new RxMaker<Race>()
                    .make(new RxRequest(RxType.ADD_RACE).longs(getTeacher().getId(), getStudent().getId()).object(race))
                    .subscribe(new Subscriber<Race>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Race race) {
                            if (race != null){
                                currentRace = race;
                                generateRandomQuestion();
                                numQuestions = currentRace.getNumQuestions();
                            }
                        }
                    });
        }

    }
}
