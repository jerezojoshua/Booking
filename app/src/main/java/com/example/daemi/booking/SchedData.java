package com.example.daemi.booking;

/**
 * Created by Joshua on 5/11/2016.
 */
public class SchedData {

    private String pickup, dropoff, date, time;

    public SchedData(String pickup, String dropoff, String date, String time)
    {
        this.setPickup(pickup);
        this.setDropoff(dropoff);
        this.setDate(date);
        this.setTime(time);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDropoff() {
        return dropoff;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }
}
