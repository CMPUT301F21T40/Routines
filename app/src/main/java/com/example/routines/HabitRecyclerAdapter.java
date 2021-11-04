package com.example.routines;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.MyViewHolder>
implements ReorderHabits.RecyclerTouchHelper {
    private ArrayList<Habit> habits;
    private OnHabitClickListener onHabitClickListener;
    int start;
    int end;
    int loopNum;

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

    @Override
    public void onRowMoved(int from, int to) {
        if(from < to){
            for(int i = from; i < to; i++){
                Collections.swap(habits, i, i+1);

            }
        }else{
            for(int i = from; i > to; i--){
                Collections.swap(habits, i, i-1);

            }
        }
        notifyItemMoved(from, to);

        Log.d("Reorder Begin", Integer.toString(from));
        Log.d("Reorder End", Integer.toString(to));

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    public void updateDocIndex() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Habits")
                .document(userId)
                .collection("Habits");
        int length = habits.size();
        int i;
        for (i = 0; i < length; i++) {
            String habitName = habits.get(i).getName();
            //Map<String, Object> data = new HashMap<>();
            //data.put("Index", i);
            int finalI = i;
            collectionReference
                    .whereEqualTo("Habit Name", habitName)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    collectionReference.document(document.getId())
                                            .update("Index", finalI);
                                }
                            }
                        }
                    });


        }
        collectionReference.orderBy("Index", Query.Direction.ASCENDING);


    }
}
