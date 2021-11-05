package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

/**
 * Unit Test for US 01.05.01
 * Tests the DeleteHabit Story
 * Issue: Test events were not received
 * @author ipaterso
 */

public class DeleteHabitUnitTest {
    private HabitList habitList;
    private Habit mockHabit;
    private ArrayList<String> frequency = new ArrayList<>();
    private Context mockContext = mock(Context.class);

    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        habitList = new HabitList(mockContext, new ArrayList<>());
        frequency.add("Monday");
        frequency.add("Thursday");
        mockHabit = new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
        habitList.add(mockHabit);
    }

    @Test
    public void deleteHabitTest() {
        habitList.remove(mockHabit);
        assertEquals(0, habitList.habitCount());
    }
}
