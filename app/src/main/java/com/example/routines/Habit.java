package com.example.routines;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Initial Habit class to store various information about a habit
 */
public class Habit implements Serializable {
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
    public ArrayList<String> frequency;
    public String privacy;

    /**
     * Initializer for the habit
     * @param name
     * @param reason
     * @param date
     * @param privacy
     */
    public Habit(String name, String reason, String date, ArrayList<String> frequency, String privacy) {
        this.name = name;
        this.reason = reason;
        this.date = date;
        this.frequency = frequency;
        this.privacy = privacy;
    }

    public Habit(String name, String reason, String date) {
        this.name = name;
        this.reason = reason;
        this.date = date;
    }

    //    Setters

    /**
     * set the name of the habit
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set the reason of the habit
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * sets the date of the habit
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * sets the privacy attribute of a habit
     * @param privacy
     */
    public void setPrivacy(String privacy){
        this.privacy = privacy;
    }

    /**
     * sets the frequency of the habit
     * @param freq
     */
    public void setFrequency(ArrayList<String> freq){
        this.frequency = freq;
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

    /**
     * Gets the days the habit occurs on
     * @return
     * ArrayList frequency
     */
    public List<String> getFrequency() {
        return this.frequency;
    }

    /**
     * returns the current setting of privacy
     * @return
     */
    public String getPrivacy(){
        return this.privacy;
    }

    public boolean underNameLimit() {
        return name.replace(" ", "").length() <= 20;
    }

    public boolean underReasonLimit() {
        return reason.replace(" ", "").length() <= 30;
    }

}
