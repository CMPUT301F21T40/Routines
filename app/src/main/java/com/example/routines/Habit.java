package com.example.routines;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Initial Habit class to store various information about a habit
 * @see HabitList Class
 * @author lwaschuk
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
    public String lastModifiedDate;
    public String lastCompletionTime;
    public String completionTime;
    public String estimateCompletionDate;

    private long progress;

    /**
     * @param name
     * @param reason
     * @param date
     * @param frequency
     * @param privacy
     * @param completionTime
     * @param estimateCompletionDate
     * @param lastCompletionTime
     * @param lastModifiedDate
     * @param progress
     * @author lwaschuk
     */
    public Habit(String name, String reason, String date, ArrayList<String> frequency, String privacy, String completionTime, String estimateCompletionDate, String lastCompletionTime, String lastModifiedDate, long progress) {
        this.name = name;
        this.reason = reason;
        this.date = date;
        this.frequency = frequency;
        this.privacy = privacy;
        this.progress = progress;
        this.completionTime = completionTime;
        this.estimateCompletionDate = estimateCompletionDate;
        this.lastCompletionTime = lastCompletionTime;
        this.lastModifiedDate = lastModifiedDate;
        this.progress = progress;
    }

    /**
     * Second initializer for habit
     * @param name String
     * @param reason String
     * @param date String
     * @see Habit Class
     * @author lwaschuk
     */
    public Habit(String name, String reason, String date, long progress) {
        this.name = name;
        this.reason = reason;
        this.date = date;
        this.progress = progress;
    }

    /**
     * set the name of the habit
     * @param name String
     * @see Habit Class
     * @author lwaschuk
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set the reason of the habit
     * @param reason String
     * @see Habit Class
     * @author lwasachuk
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * sets the date of the habit
     * @param date String
     * @see Habit Class
     * @author lwaschuk
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * sets the privacy attribute of a habit
     * @param privacy String
     * @see Habit Class
     * @author lwaschuk
     */
    public void setPrivacy(String privacy){
        this.privacy = privacy;
    }

    /**
     * sets the frequency of the habit
     * @param freq String
     * @see Habit Class
     * @author lwaschuk
     */
    public void setFrequency(ArrayList<String> freq){
        this.frequency = freq;
    }
    /**
     * Gets the name of the habit
     * @return name String
     * @see Habit Class
     * @author lwaschuk
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the reason of the habit
     * @return String reason
     * @see Habit Class
     * @author lwaschuk
     */
    public String getReason(){
        return this.reason;
    }

    /**
     * Gets the Date started of the habit
     * @return String Date
     * @see Habit Class
     * @author lwasachuk
     */
    public String getDate(){
        return this.date;
    }

    /**
     * Gets the days the habit occurs on
     * @return ArrayList frequency
     * @see Habit Class
     * @author lwaschuk
     */
    public List<String> getFrequency() {
        return this.frequency;
    }

    /**
     * returns the current setting of privacy
     * @return privacy String
     * @see Habit Class
     * @author lwaschuk
     */
    public String getPrivacy(){
        return this.privacy;
    }

    /**
     * check if name is under 20 characters
     * @return boolean
     */
    public boolean underNameLimit() {
        return name.replace(" ", "").length() <= 20;
    }

    /**
     * check if reason is under 30 characters
     * @return boolean
     */
    public boolean underReasonLimit() {
        return reason.replace(" ", "").length() <= 30;
    }

    /**
     * Returns the percentage of habit we are completing on time
     * @return long progress
     * @author lukas
     * @see Habit
     */
    public long getProgress() {
        return this.progress;
    }

    /**
     * Allows the progress to be set and savaed to firebase
     * @param progress
     * @author lukas
     * @see Habit
     */
    public void setProgress(long progress) {
        this.progress = progress;
    }

    /**
     * return the times of completion the user should finish before the user edit the frequency
     * @return String lastCompletionTime
     * @author zezhou
     * @see Habit
     */
    public String getLastCompletionTime() {
        return lastCompletionTime;
    }

    /**
     * set the times of completion the user should finish before the user edit the frequency
     * @param lastCompletionTime
     * @author zezhou
     * @see Habit
     */
    public void setLastCompletionTime(String lastCompletionTime) {
        this.lastCompletionTime = lastCompletionTime;
    }

    /**
     * return the times of completion the user already finished
     * @return String completionTime
     * @author zezhou
     * @see Habit
     */
    public String getCompletionTime() {
        return completionTime;
    }

    /**
     * set the times of completion the user already finished
     * @param completionTime
     * @author zezhou
     * @see Habit
     */
    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    /**
     * return the times of completion the user should finish in total
     * @return String estimateCompletionDate
     * @author zezhou
     * @see Habit
     */
    public String getEstimateCompletionDate() {
        return estimateCompletionDate;
    }

    /**
     * set the times of completion the user should finish in total
     * @param estimateCompletionDate
     * @author zezhou
     * @see Habit
     */
    public void setEstimateCompletionDate(String estimateCompletionDate) {
        this.estimateCompletionDate = estimateCompletionDate;
    }

    /**
     * return the last date the user change the frequency
     * @return String lastModifiedDate
     * @author zezhou
     * @see Habit
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * set the last date the user change the frequency
     * @param lastModifiedDate
     * @author zezhou
     * @see Habit
     */
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
