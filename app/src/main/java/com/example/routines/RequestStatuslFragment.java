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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RequestStatuslFragment extends DialogFragment {

    private EditText requestStatus;
    private Request request;
    CollectionReference requestReference;
    FirebaseFirestore db;
    FirebaseAuth myAuth;
    private RequestStatuslFragment.RespondFragmentInteractionListener listener;

    public interface RespondFragmentInteractionListener{
        void onYesPressed();
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
        if(context instanceof RespondFragmentInteractionListener){
            listener = (RespondFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()+"must implement OnFragmentInteractionListener");
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

                        db = FirebaseFirestore.getInstance();
                        requestReference = db.collection("Notification");
                        myAuth = FirebaseAuth.getInstance();
                        String userId = myAuth.getCurrentUser().getUid();
                        requestReference
                                .whereEqualTo("Sender Name", name)
                                .whereEqualTo("Receiver", userId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                requestReference
                                                        .document(document.getId())
                                                        .update("Status", status)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d("Updated Request", document.getId() + " => " + document.getData());
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                });
                        listener.onYesPressed();
                    }
                }).create();
    }
}
