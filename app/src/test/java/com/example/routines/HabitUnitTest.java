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
        habit = new Habit("Soccer", "It is fun", "2020-01-01", frequency, "Private","20", "15","10", "2021-01-01",  0);
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
    public void getProgressTest() {
        long progress = habit.getProgress();
        assertEquals(0, progress);
    }

    @Test
    public void getCompletionTimeTest() {
        String completionTime = habit.getCompletionTime();
        assertEquals(completionTime, "20");
    }

    @Test
    public void getEstimateCompletionTimeTest() {
        String estimateCompletionTime = habit.getEstimateCompletionDate();
        assertEquals(estimateCompletionTime, "15");
    }

    @Test
    public void getLastCompletionTimeTest() {
        String lastCompletionTime = habit.getLastCompletionTime();
        assertEquals(lastCompletionTime, "10");
    }

    @Test
    public void getLastModifiedDateTest() {
        String lastModifiedDate = habit.getLastModifiedDate();
        assertEquals(lastModifiedDate, "2021-01-01");
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

    @Test
    public void setProgressTest() {
        long newProgress = 12;
        habit.setProgress(newProgress);
        long updatedProgress = habit.getProgress();
        habit.setProgress(updatedProgress);
        assertEquals(12, updatedProgress);
    }

    @Test
    public void setCompletionTime() {
        String newCompletionTime = "30";
        habit.setCompletionTime(newCompletionTime);
        String updatedCompletionTime = "33";
        habit.setCompletionTime(updatedCompletionTime);
        assertEquals("33", updatedCompletionTime);
    }

    @Test
    public void setEstimateCompletionDate (){
        String newEstimateCompletionDate = "2030-01-01";
        habit.setEstimateCompletionDate(newEstimateCompletionDate);
        String updatedEstimateCompletionDate = "2040-01-01";
        habit.setEstimateCompletionDate(updatedEstimateCompletionDate);
        assertEquals("2040-01-01", updatedEstimateCompletionDate);
    }

    @Test
    public void setLastCompletionTime (){
        String newLastCompletionTime = "40";
        habit.setLastCompletionTime(newLastCompletionTime);
        String updatedLastCompletionTime = "50";
        habit.setLastCompletionTime(updatedLastCompletionTime);
        assertEquals("50", updatedLastCompletionTime);
    }

    @Test
    public void setLastModifiedDate () {
        String newLastModifiedDate = "2010-01-01";
        habit.setLastModifiedDate(newLastModifiedDate);
        String updatedLastModifiedDate = "2000-01-01";
        habit.setLastModifiedDate(updatedLastModifiedDate);
        assertEquals("2000-01-01", updatedLastModifiedDate);
    }

}
