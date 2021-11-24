package com.example.routines;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class RequestStatuslFragment extends DialogFragment {

    private EditText requestStatus;
    private Request request;

    private RequestStatuslFragment.RespondFragmentInteractionListener listener;

    public interface RespondFragmentInteractionListener {
        void onYesPressed(Request request);
    }

    static RequestStatuslFragment newInstance(Request request){
        Bundle args = new Bundle();
        args.putSerializable("Request", request);
        RequestStatuslFragment fragment = new RequestStatuslFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof AddHabitFragment.OnFragmentInteractionListener) {
            listener = (RequestStatuslFragment.RespondFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_request_detail, null);
        requestStatus = view.findViewById(R.id.textView_requestFragment_Status);
        Bundle arguments = getArguments();
        if(arguments != null){
            request = (Request) arguments.getSerializable("Request");
            requestStatus.setText(request.getStatus());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit/View request details")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = request.getRequestUser();
                        String status = requestStatus.getText().toString();
                        listener.onYesPressed(new Request(name, status));
                    }
                }).create();
    }
}
