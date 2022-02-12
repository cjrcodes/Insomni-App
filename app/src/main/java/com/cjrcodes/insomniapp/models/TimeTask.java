package com.cjrcodes.insomniapp.models;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Christian Rodriguez
 * TimeTask class, an instance of this class contains all information needed to start a timed session of tracking
 */
public class TimeTask implements Serializable {
    //Elapsed time measurement
    private long time;
    //The max heart rate threshold, the average heart rate must be greater than this value in order for the alarm to go off.
    private int maxHeartRate;
    //Heart rate measurement type which decides how heart rate will be compared to the max, average over time vs. current heart rate
    private HeartRateMeasurementType hrMeasureType;
    //Amount of time to measure average, can be specified by the user
    private long averageMeasurementTime;
    //Specifies whether alarm is checking a clock time or an elapsed time
    private AlarmType alarmType;

    /**
     * Default TimeTask Constructor, defaults to elapsed time, measures the average of the last minute in a 15 minute time block
     */
    public TimeTask() {
        //Defaults to elapsed time
        this.alarmType = AlarmType.ELAPSED_TIME;
        //Safe threshold set at a default of 70, the average heart rate for a person sleeping is around 50-60 bpm
        this.maxHeartRate = 70;
        //15 Minutes in milliseconds
        this.time = 900000;
        //Average is used for default
        this.hrMeasureType = HeartRateMeasurementType.AVERAGE;
        //1 Minute average measurement
        this.averageMeasurementTime = 60000;
    }

    /**
     * TimeTask Constructor, comes from user input
     *
     * @param alarmType              Type of alarm, either elapsed time or clock time
     * @param averageMeasurementTime The length of time to measure the average heart rate
     * @param time                   How long elapsed time should last or when the heart rate measurement should be checked at a specific clock time, both in milliseconds
     * @param maxHeartRate           Sets the max heart rate threshold, if the heart rate measurement is above this value, the alarm will go off as the user is awake
     * @param hrMeasureType          Type of heart rate measurement, either checks the average of the specified average measurement time, or checks the current heart rate when the time block is over
     */
    public TimeTask(AlarmType alarmType, long averageMeasurementTime, long time, int maxHeartRate, HeartRateMeasurementType hrMeasureType) {
        this.alarmType = alarmType;
        this.averageMeasurementTime = averageMeasurementTime;
        this.time = time;
        this.maxHeartRate = maxHeartRate;
        this.hrMeasureType = hrMeasureType;
    }

    public String convertMillisecondsToHourMinuteSecond(long time) {
        long HH = TimeUnit.MILLISECONDS.toHours(time);
        long MM = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long SS = TimeUnit.MILLISECONDS.toSeconds(time) % 60;
        String formattedString = String.format("%02d:%02d:%02d", HH, MM, SS);

        return formattedString;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public HeartRateMeasurementType getHrMeasureType() {
        return hrMeasureType;
    }

    public void setHrMeasureType(HeartRateMeasurementType hrMeasureType) {
        this.hrMeasureType = hrMeasureType;
    }

    public long getAverageMeasurementTime() {
        return averageMeasurementTime;
    }

    public void setAverageMeasurementTime(long averageMeasurementTime) {
        this.averageMeasurementTime = averageMeasurementTime;
    }

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
    }

    public static ArrayList<TimeTask> createTimeTaskList(int numOfTimeTasks) {
        ArrayList<TimeTask> timeTasks = new ArrayList<TimeTask>();
        for (int i = 1; i <= numOfTimeTasks; i++) {
            if(i % 2 != 0){
                timeTasks.add(new TimeTask(AlarmType.CLOCK_TIME, 60000, 0, 70, HeartRateMeasurementType.CURRENT ));
            }

            else{
                timeTasks.add(new TimeTask(AlarmType.ELAPSED_TIME, 60000, 900000, 70, HeartRateMeasurementType.AVERAGE ));

            }
        }
        return timeTasks;
    }
}
