package com.example.routines;

public class Event {
    public String eventName;
    public String description;
    public String habitID;
    public String eventDate;

    public Event(String eventName, String description, String habitID, String eventDate) {
        this.eventName = eventName;
        this.description = description;
        this.habitID = habitID;
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHabitID() {
        return habitID;
    }

    public void setHabitID(String habitID) {
        this.habitID = habitID;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
