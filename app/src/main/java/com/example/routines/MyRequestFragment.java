package com.example.routines;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This fragment will show the requests of pending status I sent to other users
 * @author Shanshan Wei/swei3
 */
public class MyRequestFragment extends Fragment {

    private View rootView;
    FrameLayout fragmentLayout;
    FirebaseFirestore db;
    CollectionReference requestReference;
    String userId;
    FirebaseAuth myAuth;
    ListView myRequestListView;
    private ArrayList<Request> requestList;
    private NotificationAdapter requestAdapter;


    /**
     * Constructor
     */
    public MyRequestFragment() {
        // Required empty public constructor
    }


    public static MyRequestFragment newInstance() {
        MyRequestFragment fragment = new MyRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflate the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     * @author Shanshan Wei/swei3
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_request, container, false);
        return rootView;
    }

    /**
     * Create the view for the fragment
     * show the requests of pending status I sent to other users
     * @param view
     * @param savedInstanceState
     * @return void
     * @author Shanshan Wei/swei3
     *
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        fragmentLayout = rootView.findViewById(R.id.container);
        myRequestListView = rootView.findViewById(R.id.fragment_my_request_list);
        requestList = new ArrayList<>();
        requestAdapter = new NotificationAdapter(getContext(), requestList);
        myRequestListView.setAdapter(requestAdapter);

        db = FirebaseFirestore.getInstance();
        myAuth = FirebaseAuth.getInstance();
        userId = myAuth.getCurrentUser().getUid();
        requestReference = db.collection("Notification");
        requestReference
                .whereEqualTo("Sender", userId)
                .whereEqualTo("Status", "pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            boolean isEmpty = task.getResult().isEmpty();
                            if(isEmpty){
                                Log.d("My Request", "null");
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("My Request", document.getId() + " => " + document.getData());
                                    requestReference
                                            .document(document.getId())
                                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.w("Get request", "Listen failed.", error);
                                                        return;
                                                    }

                                                    if (value != null && value.exists()) {
                                                        Log.d("Get request", "Current data: " + value.getData());
                                                        String name = (String) value.getData().get("Receiver Name");
                                                        String  status = (String) value.getData().get("Status");
                                                        Request request = new Request(name, status);
                                                        requestList.add(request);
                                                        requestAdapter.notifyDataSetChanged();
                                                    } else {
                                                        Log.d("get request", "Current data: null");
                                                    }
                                                }
                                            });
                                    requestAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

    }

}