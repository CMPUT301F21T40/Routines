package com.example.routines;

import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;

public class FollowingUnitTest {
    private Habit mockHabit;
    private ArrayList<String> frequency = new ArrayList<>();
    HashMap<String, String> data;

    @BeforeEach
    public void createNotification(){ // create a new mock habit list for every test
        data = new HashMap<>();
        data.put("Sender Name", "john123");
        data.put("Receiver Name", "emma565");
    }


}
