package com.cjrcodes.insomniapp;

import java.io.Serializable;
import java.sql.Time;

/**
 * TimeTask class, an instance of this class contains all information needed to start a timed session of tracking
 *
 */
public class TimeTask implements Serializable {

    //
    private long countDownTime;

    //
    private Time time;

    //The max heart rate threshold, the average heart rate must be greater than this value in order for the alarm to go off.
    private int maxHeartRate;

    //Default time task
    public TimeTask() {
        //Safe threshold set at a default of 70, the average heart rate for a person sleeping is around 50-60 bpm
        this.maxHeartRate = 70;


    }




}
