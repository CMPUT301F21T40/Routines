package com.example.routines;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Unit tests of Habit frequency
 * @author yyang13
 */

public class FrequencyUnitTest {
    private ArrayList<String> freuency;

    @BeforeEach
    public void createFrequencyList() {
         freuency = new ArrayList<>();
    }


    @Test
    public void addFrequencyTest() {
        String mon = "Monday";
        freuency.add(mon);
        assertEquals(1, freuency.size());
    }

    @Test
    public void hasFrequencyTest() {
        String tue = "Tuesday";
        assertFalse(freuency.contains(tue));
        freuency.add(tue);
        assertTrue(freuency.contains(tue));
    }

    @Test
    public void countFrequencyTest() {
        int before = freuency.size();
        assertEquals(0, before);
        freuency.add("Wednesday");
        int after = freuency.size();
        assertEquals(1, after);
    }

    @Test
    public void getFrequencyTest() {
        String thu = "Thursday";
        freuency.add(thu);
        String newThu = freuency.get(0);
        assertEquals(newThu, thu);
    }

}
