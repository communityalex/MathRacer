package com.community.zenith.mathracer.studentsactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.zenith.mathracer.R;
import com.community.zenith.mathracer.UtilManager;
import com.example.alex.mathracer.backend.mathRacerApi.model.Race;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 20/10/2016.
 */
public class RacesAdapter extends RecyclerView.Adapter<RacesAdapter.ViewHolder> {

    private List<Race> races;
    private Context context;
    protected ClickListener clickListener;

    public RacesAdapter(List<Race> races, Context context) {
        this.context = context;
        this.races = races;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.questions) TextView questions;
        @BindView(R.id.time_ago) TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public RacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.race_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RacesAdapter.ViewHolder holder, int position) {
        Race race = races.get(position);
        holder.questions.setText(String.valueOf(race.getNumCorrect()) + "/" + String.valueOf(race.getNumQuestions()));
        holder.time.setText(UtilManager.timeSince(race.getStartTime()));
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}