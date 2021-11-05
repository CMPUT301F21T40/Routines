package com.example.routines;

import static org.mockito.Mockito.mock;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EditHabitUnitTest {
    private Habit mockHabit;
    private HabitList mockList;
    private ArrayList<String> frequency = new ArrayList<>();
    private TextView mockTextView = mock(TextView.class);
    private Context mockContext = mock(Context.class);
    private View mockView = mock(View.class);

    @Before
    public void createHabit(){ // create a new mock habit list for every test
        frequency.add("Monday");
        frequency.add("Thursday");
        mockHabit = new Habit("Piano", "Good for me", "2020-05-27",frequency, "Private");
        mockList = new HabitList(mockContext, new ArrayList<>());
        mockList.addHabit(mockHabit);
    }

    @Test
    public void editHabitTest() {

    }

}
