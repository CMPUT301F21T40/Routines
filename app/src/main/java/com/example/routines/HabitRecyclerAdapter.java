package com.example.routines;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.MyViewHolder> {
    private ArrayList<Habit> habits;
    private OnHabitClickListener onHabitClickListener;

    public HabitRecyclerAdapter(ArrayList<Habit> habits, OnHabitClickListener onHabitClickListener) {
        this.habits = habits;
        this.onHabitClickListener = onHabitClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView habitNameText;
        private TextView habitReasonText;
        private TextView habitDateText;
        OnHabitClickListener onHabitClickListener;
        public MyViewHolder(View view, OnHabitClickListener onHabitClickListener){
            super(view);
            habitNameText = view.findViewById(R.id.habitName);
            habitReasonText = view.findViewById(R.id.habitReason);
            habitDateText = view.findViewById(R.id.habitDate);
            this.onHabitClickListener = onHabitClickListener;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onHabitClickListener.onItemClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public HabitRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list, parent, false);
        return new MyViewHolder(itemView, onHabitClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitRecyclerAdapter.MyViewHolder holder, int position) {
        String name = habits.get(position).getName();
        String reason = habits.get(position).getReason();
        String date = habits.get(position).getDate();
        holder.habitNameText.setText(name);
        holder.habitReasonText.setText(reason);
        holder.habitDateText.setText(date);
    }

    @Override
    public int getItemCount() {
        return habits.size() ;
    }

    public interface OnHabitClickListener {
        void onItemClick(int position);
    }
}
