package com.example.routines;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This extends RecyclerView.Adapter and customized the adapter
 * @author Shanshan Wei/swei3
 * @see com.example.routines.ReorderHabits.RecyclerTouchHelper
 */
public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.MyViewHolder>
implements ReorderHabits.RecyclerTouchHelper {
    private ArrayList<Habit> habits;
    private OnHabitClickListener onHabitClickListener;
    private ArrayList<String> userName;

    /**
     * Constructor of this adapter is used to initialzied the adapter in other activities
     * @author Shanshan Wei/swei3
     * @param habits
     * @param onHabitClickListener
     */
    public HabitRecyclerAdapter(ArrayList<Habit> habits, OnHabitClickListener onHabitClickListener) {
        this.habits = habits;
        this.onHabitClickListener = onHabitClickListener;
    }

    /**
     * Constructor used to view the habits of all users the current user follows
     * @param habits
     * @param userName
     * @param onHabitClickListener
     * @author ipaterso
     */
    public HabitRecyclerAdapter(ArrayList<Habit> habits, ArrayList<String> userName, OnHabitClickListener onHabitClickListener) {
        this.habits = habits;
        this.userName = userName;
        this.onHabitClickListener = onHabitClickListener;
    }


    /**
     * This view holder extends Recycler.ViewHolder and sets the attributes for items to show
     * It also override the click listener.
     * @author Shanshan Wei/swei3
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView habitNameText;
        private TextView habitUserText;
        private TextView habitReasonText;
        private TextView habitDateText;
        OnHabitClickListener onHabitClickListener;

        private ProgressBar progressbar;

        public MyViewHolder(View view, OnHabitClickListener onHabitClickListener){
            super(view);
            habitNameText = view.findViewById(R.id.habitName);
            habitUserText = view.findViewById(R.id.habitUser);
            progressbar = view.findViewById(R.id.progressBar3);

            this.onHabitClickListener = onHabitClickListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHabitClickListener.onItemClick(getAdapterPosition());
        }
    }

    /**
     * This method create the view holder for adapter
     * @author Shanshan Wei/swei3
     * @param parent
     * @param viewType
     * @return HabitRecyclerAdapter.MyViewHolder
     */
    @NonNull
    @Override
    public HabitRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list, parent, false);
        return new MyViewHolder(itemView, onHabitClickListener);
    }

    /**
     * This method help the adapter get and set the attributes of event
     * @author Shanshan Wei/swei3
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull HabitRecyclerAdapter.MyViewHolder holder, int position) {
        String name = habits.get(position).getName();
        long progress = habits.get(position).getProgress(); // stored as a long must convert it to update it
        holder.habitNameText.setText(name);

        if (userName != null) {
            String user = userName.get(position);
            holder.habitUserText.setText(user);
        }
        int i = (int) progress; // convert to int, must store as long
        holder.progressbar.setProgress(i);
    }

    /**
     * This counts the items showed by this adapter
     * @author Shanshan Wei/swei3
     * @return int
     */
    @Override
    public int getItemCount() {
        return habits.size() ;
    }

    /**
     * This creates a listener for every list item showed by adapter
     * @author Shanshan Wei/swei3
     */
    public interface OnHabitClickListener {
        void onItemClick(int position);
    }

    /**
     * It records the item row movement and swaps the items rows between the two positions
     * @author Shanshan Wei/swei3
     * @param from
     * @param to
     */
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
        updateDocIndex();
    }

    /**
     * This will set the background color of the selected row to gray
     * @author Shanshan Wei/swei3
     * @param myViewHolder
     */
    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.GRAY);
    }

    /**
     * This clears the background color of selected item row
     * @author Shanshan Wei/swei3
     * @param myViewHolder
     * TODO: 2021-11-27 this is changing the color to white after we click and not the current background color
     */
    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    /**
     * This will records the items' positions and update Index filed on firebase base on the position
     * @author Shanshan Wei/swei3
     */
    public void updateDocIndex() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Habits")
                .document(userId)
                .collection("Habits");
        int length = habits.size();

        for (int i = 0; i < length; i++) {
            String habitName = habits.get(i).getName();
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
    }
}
