package com.example.routines;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ReorderHabits extends ItemTouchHelper.Callback {

    private RecyclerTouchHelper recyclerTouchHelper;

    public ReorderHabits(RecyclerTouchHelper recyclerTouchHelper) {
        this.recyclerTouchHelper = recyclerTouchHelper;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlag,0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.recyclerTouchHelper.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof HabitRecyclerAdapter.MyViewHolder){
                HabitRecyclerAdapter.MyViewHolder myViewHolder = (HabitRecyclerAdapter.MyViewHolder) viewHolder;
                recyclerTouchHelper.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof HabitRecyclerAdapter.MyViewHolder){
            HabitRecyclerAdapter.MyViewHolder myViewHolder = (HabitRecyclerAdapter.MyViewHolder) viewHolder;
            recyclerTouchHelper.onRowClear(myViewHolder);
        }

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface RecyclerTouchHelper{
        void onRowMoved(int from, int to);
        void onRowSelected(HabitRecyclerAdapter.MyViewHolder myViewHolder);
        void onRowClear(HabitRecyclerAdapter.MyViewHolder myViewHolder);
    }
}
