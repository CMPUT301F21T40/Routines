package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class HabitListUnitTest {
    private ArrayList<Habit> habits;
    private ArrayList<String> frequency = new ArrayList<>();


    private HabitList mockHabitList(){
        HabitList habitList = new HabitList(null, habits);
        habitList.add(mockHabit());
        return habitList;
    }

    private Habit mockHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        return new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }

    @Test
    public void addHabitTest(){
        HabitList habitList =  mockHabitList();
        assertEquals(1, habitList.habitCount());

    }

    public void hasHabitTest(){

    }

    public void countHabitTest(){

    }
}
