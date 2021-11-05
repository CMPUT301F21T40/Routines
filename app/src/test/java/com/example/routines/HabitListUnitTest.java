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


public class HabitListUnitTest {
    private HabitList habitList;
    private ArrayList<String> frequency = new ArrayList<>();
    private Context mockContext = mock(Context.class);
    private View mockView = mock(View.class);
    private ViewGroup mockViewGroup = mock(ViewGroup.class);
    private TextView mockTextView = mock(TextView.class);


    @BeforeEach
    public void createHabitList(){ // create a new mock habit list for every test
        habitList = new HabitList(mockContext, new ArrayList<>());

    }

    private Habit stubHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        return new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }
    public void addHabit(){
        habitList.addHabit(stubHabit());
    }

    @Test
    public void addHabitTest(){
        habitList.addHabit(stubHabit());
        assertEquals(1, habitList.habitCount()); // count will be 1 if the add worked
    }

    @Test
    public void hasHabitTest(){
        Habit newHabit = stubHabit();
        assertFalse(habitList.containsHabit(newHabit)); // should be false
        habitList.addHabit(newHabit);
        assertTrue(habitList.containsHabit(newHabit)); // should be true
    }

    @Test
    public void countHabitTest(){
        int before = habitList.habitCount(); // should be 0
        habitList.addHabit(stubHabit());
        int after = habitList.habitCount(); // should be 1
        assertEquals(0, before); // check if it equals 0
        assertEquals(1, after); // check if it equals 1
    }
    @Test
    public void getViewTest(){
        // whenever view is called just return a Textview (mock)
        when(mockView.findViewById(anyInt())).thenReturn(mockTextView);
        // add a habit to the list
        habitList.addHabit(stubHabit());
        // get the view of the added habit using the mockView and mockViewGroup
        habitList.getView(0,mockView,mockViewGroup);
        // check if we can set the textView with the habit data
        verify(mockTextView, Mockito.times(1)).setText("Soccer");
        verify(mockTextView, Mockito.times(1)).setText("2020-01-01");
        verify(mockTextView, Mockito.times(1)).setText("It is fun");
    }


}
