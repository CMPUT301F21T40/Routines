package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * Unit Test for US 01.05.01
 * Tests the DeleteHabit Story
 * Issue: Test events were not received
 * @author ipaterso
 * @see DeleteHabitFragment
 */

public class DeleteHabitUnitTest {
    private HabitList habitList;
    private Habit studHabit;
    private ArrayList<String> frequency = new ArrayList<>();
    private Context mockContext = mock(Context.class);

    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        habitList = new HabitList(mockContext, new ArrayList<>());
        frequency.add("Monday");
        frequency.add("Thursday");
        studHabit = new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private", 0);
        habitList.addHabit(studHabit);
    }

    @Test
    public void deleteHabitTest() {
        habitList.removeHabit(studHabit);
        assertEquals(false, habitList.containsHabit(studHabit));
    }
}
