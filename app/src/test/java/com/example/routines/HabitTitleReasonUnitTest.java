package com.example.routines;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class HabitTitleReasonUnitTest {
    private Habit habit;
    private ArrayList<String> frequency = new ArrayList<>();

    @BeforeEach
    public void createHabit() {
        frequency.add("Monday");
        habit = new Habit("school", "I hate myself", "2020-01-01", frequency, "Private", "20", "15","10", "2021-01-01", 0);
    }

    @Test
    public void nameCountTest() {
        assertTrue(habit.underNameLimit());

        habit.setName("Play PC Games");
        assertTrue(habit.underNameLimit());

        habit.setName(" Test   Name  Test   Name Test     ");
        assertTrue(habit.underNameLimit());

        habit.setName(" Test   Name  Test   Name Test1     ");
        assertFalse(habit.underNameLimit());

    }

    @Test
    public void reasonCountTest() {
        assertTrue(habit.underReasonLimit());

        habit.setReason("I love gaming");
        assertTrue(habit.underReasonLimit());

        habit.setReason(" Test Reason Test      Reason Test   Reason  ");
        assertTrue(habit.underReasonLimit());

        habit.setReason(" Test Reason Test      Reason Test   Reason1  ");
        assertFalse(habit.underReasonLimit());

    }


}
