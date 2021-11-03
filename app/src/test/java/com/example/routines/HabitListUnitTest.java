package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;


public class HabitListUnitTest {
    private HabitList mockHabitList;
    private ArrayList<String> frequency = new ArrayList<>();

    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        mockHabitList = new HabitList(null, new ArrayList<>());
    }

    private Habit mockHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        return new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }

    @Test
    public void addHabitTest(){
        mockHabitList.addHabit(mockHabit());
        assertEquals(1, mockHabitList.habitCount()); // count will be 1 if the add worked
    }

    @Test
    public void hasHabitTest(){
        Habit newHabit = mockHabit();
        assertFalse(mockHabitList.containsHabit(newHabit)); // should be false
        mockHabitList.addHabit(newHabit);
        assertTrue(mockHabitList.containsHabit(newHabit)); // should be true
    }

    @Test
    public void countHabitTest(){
        int before = mockHabitList.habitCount(); // should be 0
        mockHabitList.addHabit(mockHabit());
        int after = mockHabitList.habitCount(); // should be 1
        assertEquals(0, before); // check if it equals 0
        assertEquals(1, after); // check if it equals 1
    }
}
