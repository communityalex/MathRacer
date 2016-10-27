package com.community.zenith.mathracer.studentsactivity;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.community.zenith.mathracer.R;
import com.example.alex.mathracer.backend.mathRacerApi.model.Question;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alex on 20/10/2016.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private ArrayList<Question> questions;
    private Context context;
    protected ClickListener clickListener;

    public QuestionsAdapter(ArrayList<Question> questions, Context context){
        this.context = context;
        this.questions = questions;
    }

    public void swap(ArrayList<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.question) TextView question;
        @BindView(R.id.answer) TextView answer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.question_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(QuestionsAdapter.ViewHolder holder, int position) {
        Question q = questions.get(position);
        holder.question.setText(q.getQuestion());
        holder.answer.setText(String.valueOf(q.getSubmitted()));
        if (q.getCorrect().equals(q.getSubmitted())){
            holder.answer.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            holder.answer.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
