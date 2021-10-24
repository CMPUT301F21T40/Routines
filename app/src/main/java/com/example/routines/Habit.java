package com.example.routines;

public class Habit {
    public String name;
    public String reason;
    public String date;

    Habit(String name, String reason, String date){
        this.name = name;
        this.reason = reason;
        this.date = date;
    }

    public String getName(){
        return this.name;
    }
    public String getReason(){
        return this.reason;
    }
    public String getDate(){
        return this.date;
    }
}
