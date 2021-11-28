package com.example.routines;

import static org.testng.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Event
 * @author yyang13
 */

public class EventUnitTest {
    private Event event;


    @BeforeEach
    public void createEvent() {
        event = new Event("Test Event", "Test Description", "Habit Id", "2020-01-01", "Edmonton");
    }

    @Test
    public void getEventNameTest() {
        String name = event.getEventName();
        assertEquals("Test Event", name);
    }

    @Test
    public void getHabitIdTest() {
        String habitId = event.getHabitID();
        assertEquals("Habit Id", habitId);
    }

    @Test
    public void getDescriptionTest() {
        String description = event.getDescription();
        assertEquals("Test Description", description);
    }

    @Test
    public void getEventDateTest() {
        String date = event.getEventDate();
        assertEquals("2020-01-01", date);
    }

    @Test void getEventLocationTest() {
        String location = event.getEventLocation();
        assertEquals("Edmonton", location);
    }


    @Test
    public void setEventNameTest() {
        String newName = "I Love Food";
        event.setEventName(newName);
        String updateName = event.getEventName();
        assertEquals("I Love Food", updateName);
    }

    @Test
    public void setEventDescriptionTest() {
        String newDescription = "Chocolate";
        event.setDescription(newDescription);
        String updateDescription = event.getDescription();
        assertEquals("Chocolate", updateDescription);
    }

    @Test
    public void setHabitIdTest() {
        String newHabitId = "newId";
        event.setHabitID(newHabitId);
        String updateHaibitId = event.getHabitID();
        assertEquals("newId", updateHaibitId);
    }

    @Test
    public void setEventDateTest() {
        String newDate = "2002-01-01";
        event.setEventDate(newDate);
        String updateDate = event.getEventDate();
        assertEquals("2002-01-01", updateDate);
    }

    @Test
    public void setEventLocationTest() {
        String newLocation = "Calgary";
        event.setEventLocation(newLocation);
        String updatedLocation = event.getEventLocation();
        assertEquals("Calgary", updatedLocation);
    }

}

