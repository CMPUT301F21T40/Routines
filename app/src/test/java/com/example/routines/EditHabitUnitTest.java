package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

/**
 * Unit Test for US 01.04.01
 * Tests the EditHabit Story
 * Issues: Test events were not received
 * @author ipaterso
 * @see EditHabitFragment
 */
public class EditHabitUnitTest {
    private Habit mockHabit;
    private ArrayList<String> frequency = new ArrayList<>();

    @BeforeEach
    public void createHabit(){ // create a new mock habit list for every test
        frequency.add("Monday");
        frequency.add("Thursday");
        mockHabit = new Habit("Piano", "Good for me", "2020-05-27",frequency, "Private");
    }

    @Test
    public void editHabitTest() {
        mockHabit.setName("Soccer");
        assertEquals("Soccer", mockHabit.getName());
        mockHabit.setReason("It's fun");
        assertEquals("It's fun", mockHabit.getReason());
        mockHabit.setDate("2020-10-12");
        assertEquals("2020-10-12", mockHabit.getDate());
        frequency.add("Saturday");
        mockHabit.setFrequency(frequency);
        assertEquals(frequency, mockHabit.getFrequency());
        mockHabit.setPrivacy("Public");
        assertEquals("Public", mockHabit.getPrivacy());
    }

}
