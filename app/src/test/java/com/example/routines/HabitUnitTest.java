package com.example.routines;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.view.View;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class HabitUnitTest {
    private Habit habit;
    private ArrayList<String> frequency = new ArrayList<>();

    @BeforeEach
    public void createHabit(){
        frequency.add("Monday");
        frequency.add("Thursday");
        habit = new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private");
    }

    @Test
    public void getNameTest(){
        String name = habit.getName();
        assertEquals("Soccer", name);
    }

    @Test
    public void getReasonTest(){
        String reason = habit.getReason();
        assertEquals("It is fun", reason);
    }

    @Test
    public void getDateTest(){
        String date = habit.getDate();
        assertEquals("2020-01-01", date);
    }

    @Test
    public void getFrequencyTest(){
        List<String> freq = habit.getFrequency();
        List<String> test = new ArrayList<>();
        test.add("Monday");
        test.add("Thursday");
        assertEquals(freq,test);
    }

    @Test
    public void getPrivacyTest() {
        String privacy = habit.getPrivacy();
        assertEquals("Private", privacy);
    }

    @Test
    public void setNameTest(){
        String newName = "Sleeping";
        habit.setName(newName);
        String updatedName = habit.getName();
        assertEquals("Sleeping", updatedName);
    }

    @Test
    public void setReasonTest(){
        String newReason = "I am tired";
        habit.setReason(newReason);
        String updatedReason = habit.getReason();
        assertEquals("I am tired", updatedReason);
    }

    @Test
    public void setDateTest(){
        String newDate = "2017-06-21";
        habit.setDate(newDate);
        String updatedDate = habit.getDate();
        assertEquals("2017-06-21", updatedDate);
    }

    @Test
    public void setFrequencyTest(){
        ArrayList<String> newFreq = new ArrayList<>();
        newFreq.add("Monday");
        newFreq.add("Thursday");
        habit.setFrequency(newFreq);
        List<String> updatedFreq = habit.getFrequency();
        assertEquals(updatedFreq,newFreq);
    }

    @Test
    public void setPrivacyTest() {
        String newPrivacy = "Public";
        habit.setPrivacy(newPrivacy);
        String updatedPrivacy = habit.getPrivacy();
        assertEquals("Public", newPrivacy);
    }

}
