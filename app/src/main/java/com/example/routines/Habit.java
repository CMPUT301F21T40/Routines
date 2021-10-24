package com.example.routines;

/**
 * Initial Habit class to store various information about a habit
 */
public class Habit {
    /*
    Purpose:
        A basic habit event that can be added to a user when we have functionality for
        unique users
     Outstanding Issues:
        None
     */
    public String name;
    public String reason;
    public String date;

    /**
     * Initializer for the habit
     * @param name
     * @param reason
     * @param date
     */
    Habit(String name, String reason, String date){
        this.name = name;
        this.reason = reason;
        this.date = date;
    }

    /**
     * Gets the name of the habit
     * @return
     * String name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the reason of the habit
     * @return
     * String reason
     */
    public String getReason(){
        return this.reason;
    }

    /**
     * Gets the Date started of the habit
     * @return
     * String Date
     */
    public String getDate(){
        return this.date;
    }
}
