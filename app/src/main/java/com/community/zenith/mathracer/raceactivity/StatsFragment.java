package com.community.zenith.mathracer.raceactivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.connection.RxMaker;
import com.community.zenith.mathracer.connection.RxRequest;
import com.community.zenith.mathracer.connection.RxType;
import com.community.zenith.mathracer.studentsactivity.RacesAdapter;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;
import com.example.alex.mathracer.backend.mathRacerApi.model.Teacher;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class StatsFragment extends Fragment {

    @BindView(R.id.stats_view) View statsView;
    @BindView(R.id.stats_name) TextView statsName;
    @BindView(R.id.stats_empty_view) View statsEmptyView;
    @BindView(R.id.stats_identifier) TextView statsIdentifier;
    @BindView(R.id.stats_graph) GraphView statsGraph;
    @BindView(R.id.races_recycler_view) RecyclerView statsRecyclerView;
    @BindView(R.id.stats_progress) View statsProgress;
    @BindView(R.id.stats_content) View statsContent;

    private RacesAdapter raceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, view);
        statsName.setText(getStudent().getFirstName() + " " + getStudent().getLastName());
        statsIdentifier.setText(getStudent().getIdentifier());
        new RxMaker<List<Race>>()
                .make(new RxRequest(RxType.GET_RACES).longs(getTeacher().getId(), getStudent().getId()))
                .subscribe(new Subscriber<List<Race>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Race> races) {
                        statsProgress.setVisibility(View.GONE);
                        if (races != null){
                            statsContent.setVisibility(View.VISIBLE);
                            statsGraph.addSeries(new LineGraphSeries<>(getDataPoints(races)));
                            statsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            raceAdapter = new RacesAdapter(races, getActivity());
                            raceAdapter.setOnItemClickListener(new RacesAdapter.ClickListener() {
                                @Override
                                public void onItemClick(int position, View v) {
                                   // showRaceView(races.get(position));
                                }

                                @Override
                                public void onItemLongClick(int position, View v) {

                                }
                            });
                            statsRecyclerView.setAdapter(raceAdapter);
                        } else {
                            statsEmptyView.setVisibility(View.VISIBLE);
                            statsContent.setVisibility(View.GONE);
                        }
                    }
                });
        return view;
    }

    public RaceActivity getRaceActivity(){
        return ((RaceActivity) this.getActivity());
    }


    public Student getStudent(){
        return getRaceActivity().getStudent();
    }

    public Teacher getTeacher(){
        return getRaceActivity().getTeacher();
    }

    private DataPoint [] getDataPoints(List<Race> races){
        DataPoint [] points = new DataPoint[races.size()];
        for (int i = 0; i < races.size(); i++){
            int perc = (int) ((double)races.get(i).getNumCorrect() / (double) races.get(i).getNumQuestions()) * 100;
            points[i] = new DataPoint(i, perc);
        }
        return points;
    }


}
