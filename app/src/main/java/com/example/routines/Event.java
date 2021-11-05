package com.example.routines;


import java.io.Serializable;

/**
 * Initial Event class to store various information about a event
 */
public class Event implements Serializable {

    /*
    Purpose:
        A basic event that can be added to a habit when we have functionality for
        each habit event
     Outstanding Issues:
        event can store a image
     */
    public String eventName;
    public String description;
    public String habitID;
    public String eventDate;
    /**
     * Initializer for the habit
     * @param eventName
     * @param description
     * @param habitID
     * @param eventDate
     */
    public Event(String eventName, String description, String habitID, String eventDate) {
        this.eventName = eventName;
        this.description = description;
        this.habitID = habitID;
        this.eventDate = eventDate;
    }

    /**
     * Constructor for the habit to initialize the Event object
     * @param eventName
     * @param description
     */
    public Event(String eventName, String description) {
        this.eventName = eventName;
        this.description = description;
        this.eventDate = eventDate;
    }

    /**
     * get the name of a event
     * @return eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * set the name of a event
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * get the description of a event
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of a event
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the habit ID of a event
     * @return habitID
     */
    public String getHabitID() {
        return habitID;
    }

    /**
     * set the habit ID of a event
     * @param habitID
     */
    public void setHabitID(String habitID) {
        this.habitID = habitID;
    }

    /**
     * get the event date of a event
     * @return eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * set the event date of a event
     * @param eventDate
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
