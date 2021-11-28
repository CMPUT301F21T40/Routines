package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * This fragment shows the event details with its name and comment
 * The user can also edit the event by inputting a new name and a new comment
 * @author Shanshan Wei/swei3
 * @see ViewEventActivity
 * @see Event
 */

public class EditEventFragment extends DialogFragment {

    private Event event;
    private EditText eventName;
    private EditText eventComment;
    private OnFragmentInteractionalListener listener;

    public interface OnFragmentInteractionalListener{
        void onOkPressed(Event event);
    }

    /**
     * This is used to transfer data of the object Event from ViewEventActivity
     * @param event
     * @return fragment
     * @author Shanshan Wei/swei3
     */
    public static EditEventFragment newInstance(Event event){
        Bundle args = new Bundle();
        args.putSerializable("Event", event);
        EditEventFragment fragment = new EditEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The start of fragments' lifecycle
     * It attaches the fragment
     * @param context
     * @author Shanshan Wei/swei3
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof  OnFragmentInteractionalListener){
            listener = (OnFragmentInteractionalListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionalListener");
        }
    }

    /**
     * This builds a dialog fragment to show details of the event and allows them to edit the details.
     * @param saveInstanceState
     * @return Dialog
     * @author Shanshan Wei/swei3
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_event, null);
        eventName = view.findViewById(R.id.editText_event_name);
        eventComment = view.findViewById(R.id.editText_event_comment);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        event = (Event) getArguments().getSerializable("Event");
        eventName.setText(event.getEventName());
        eventComment.setText(event.getDescription());


        return builder
                .setView(view)
                .setTitle("Edit Habit Event")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String inputName = eventName.getText().toString();
                        String inputComment = eventComment.getText().toString();
                        Log.d("EditEvent fragment", inputName+inputComment);
                        event.setEventName(inputName);
                        event.setDescription(inputComment);
                        listener.onOkPressed(new Event(inputName, inputComment));
                    }
                }).create();

    }
}
