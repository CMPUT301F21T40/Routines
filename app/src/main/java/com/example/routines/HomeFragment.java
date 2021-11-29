package com.example.routines;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * The fragment is used to show all the habits of the user
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment
 * @author Shanshan Wei/swei3
 * @see HabitRecyclerAdapter
 * @see HomeActivity
 * @see ViewHabitActivity
 */
public class HomeFragment extends Fragment implements HabitRecyclerAdapter.OnHabitClickListener {

    private View rootView;
    private  HabitRecyclerAdapter habitAdapter;
    private RecyclerView habitView;
    private ArrayList<Habit> habitDataList;
    private ArrayList<String> habitIdList;
    FrameLayout fragmentLayout;
    FirebaseFirestore db;
    String userId;
    FirebaseAuth myAuth;

    CollectionReference habitCollection;
    DocumentReference userDocument;
    CollectionReference currentUserHabitCol;

    /**
     * This is the constructor
     * @author Shanshan Wei/swei3
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * It is used to create an instance of fragment
     * @author Shanshan Wei/swei3
     * @return HomeFragment
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    /**
     * It inflates the fragment
     * @author Shanshan Wei/swei3
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    /**
     * This creates the fragment view
     * @param view
     * @param savedInstanceState
     */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //        Get user ID
        myAuth = FirebaseAuth.getInstance();
        FirebaseUser user = myAuth.getCurrentUser();
        userId = user.getUid();

//        Create a separate Habits collection
        db = FirebaseFirestore.getInstance();
        habitCollection = db.collection("Habits");
        userDocument = habitCollection.document(userId);
//        Sub-collection of Habit under the current user
        currentUserHabitCol = userDocument.collection("Habits");



        fragmentLayout = rootView.findViewById(R.id.container);

        // creating a listview and the adapter so we can store all the habits in a list on the home screen
        habitView = rootView.findViewById(R.id.home_fragment_habitList);

        habitDataList = new ArrayList<>();
        habitIdList = new ArrayList<>();
        habitAdapter = new HabitRecyclerAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        initHabitOrder();
        showList();
        habitView.setAdapter(habitAdapter);

    }

    /**
     * This helps to clear the adapter
     * @author Shanshan Wei/swei3
     */
    public void clear() {
        int size = habitDataList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                habitDataList.remove(0);
            }
            habitAdapter.notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * This overrides the onItemClick method in HabitRecyclerAdapter
     * When the user clicks on the item, it will direct the user to ViewHabitActivity
     * @author Shanshan Wei/swei3
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ViewHabitActivity.class);
        String habitId = habitIdList.get(position);
        intent.putExtra("sameUser", true);
        intent.putExtra("habitId", habitId);
        Log.d("Intent habit id", habitId);
        startActivity(intent);
    }

    /**
     * This attaches the ReorderHabits which is a subclass of ItemTouchHelper to the Recyclerview
     * It will show all the habits of the user
     * @author Shanshan Wei/swei3
     */
    public void initHabitOrder(){
        ItemTouchHelper.Callback callback = new ReorderHabits(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitView);
        showList();
    }

    /**
     * Add habits from Firestore to local habit list
     * @return void
     * @author yyang13
     */
    public void showList(){
        Query currentUserCol = currentUserHabitCol.orderBy("Index", Query.Direction.ASCENDING);
        currentUserCol.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                clear();
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    /**
                    int habitProgress = 0;
                    try {
                        habitProgress = (int)doc.getData().get("Progress");
                    } catch (Exception e) {
                        System.out.println("TESTTT");
                        System.out.println(e);
                    }**/

                    long habitProgress = (long)doc.getData().get("Progress");
                    String habitName = (String)doc.getData().get("Habit Name");
                    String habitReason = (String)doc.getData().get("Habit Reason");
                    String habitDate = (String)doc.getData().get("Start Date");
                    ArrayList<String> frequency = (ArrayList<String>) doc.getData().get("Frequency");
                    String privacy = (String) doc.getData().get("Privacy"); // recently added
                    String completionTime = (String) doc.getData().get("Completion Time");
                    String estimateCompletionTime = (String) doc.getData().get("Estimate Completion Time");
                    String lastCompletionTime = (String) doc.getData().get("Last Completion Time");
                    String lastModifiedDate = (String) doc.getData().get("Last Modified Date");
                    String id = (String) doc.getId();
                    habitIdList.add(id);
                    habitDataList.add(new Habit(habitName, habitReason, habitDate, frequency, privacy, completionTime, estimateCompletionTime, lastCompletionTime, lastModifiedDate, habitProgress));
                    habitAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}