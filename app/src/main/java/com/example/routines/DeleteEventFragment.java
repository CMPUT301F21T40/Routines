package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteEventFragment extends DialogFragment {
    String eventId;

    private DeleteEventFragment.OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onDeletePressedEvent(String eventId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DeleteEventFragment.OnFragmentInteractionListener) {
            listener = (DeleteEventFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    /**
     * Receives an instance of a event that is to be deleted
     * @param eventId
     * @return
     */
    static DeleteEventFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putSerializable("event", eventId);

        DeleteEventFragment fragment = new DeleteEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.confirm_delete, null);
        Bundle bundle = getArguments();
        eventId = (String) bundle.getSerializable("event");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Are you sure you want to delete this event?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeletePressedEvent(eventId);
                    }
                }).create();
    }
}
