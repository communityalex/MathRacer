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
import com.example.alex.mathracer.backend.mathRacerApi.model.Student;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 19/10/2016.
 */
public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

    private List<Student> students;
    private Context context;
    protected ClickListener clickListener;

    public StudentsAdapter(List<Student> students, Context context) {
        this.context = context;
        this.students = students;
    }

    public void swap(List<Student> students) {
        this.students = students;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.identifier) TextView identifier;
        @BindView(R.id.last_race) TextView lastRace;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.student_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StudentsAdapter.ViewHolder holder, int position) {
        Student student = students.get(position);
        if (student != null){
            if (student.getIdentifier() != null){
                holder.identifier.setText(student.getIdentifier());
            }

            holder.name.setText(student.getFirstName() + " " + student.getLastName());
            holder.lastRace.setText(UtilManager.timeSince(student.getLastRaceTime()));
        }
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}


