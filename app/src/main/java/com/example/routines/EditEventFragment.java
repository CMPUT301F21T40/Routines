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

public class EditEventFragment extends DialogFragment {

    private Event event;
    private EditText eventName;
    private EditText eventComment;
    private OnFragmentInteractionalListener listener;

    public interface OnFragmentInteractionalListener{
        void onOkPressed(Event event);
    }

    public static EditEventFragment newInstance(Event event){
        Bundle args = new Bundle();
        args.putSerializable("Event", event);
        EditEventFragment fragment = new EditEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
                .setTitle("Edit Event")
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
