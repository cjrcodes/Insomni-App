package com.cjrcodes.insomniapp.models;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Christian Rodriguez
 * TimeTask class, an instance of this class contains all information needed to start a timed
 * session of tracking
 */
public class TimeTask implements Serializable {
    //Elapsed time measurement
    private LocalTime time;
    //The max heart rate threshold, the average heart rate must be greater than this value in
    // order for the alarm to go off.
    private int maxHeartRate;
    //Heart rate measurement type which decides how heart rate will be compared to the max,
    // average over time vs. current heart rate
    private HeartRateMeasurementType hrMeasureType;
    //Amount of time to measure average, can be specified by the user
    private LocalTime averageMeasurementTime;
    //Specifies whether alarm is checking a clock time or an elapsed time
    private AlarmType alarmType;

    /**
     * Default TimeTask Constructor, defaults to elapsed time, measures the average of the last
     * minute in a 15 minute time block
     */
    public TimeTask() {
        //Defaults to elapsed time
        this.alarmType = AlarmType.ELAPSED_TIME;
        //Safe threshold set at a default of 70, the average heart rate for a person sleeping is
        // around 50-60 bpm
        this.maxHeartRate = 70;
        //15 Minutes in milliseconds
        this.time = LocalTime.of(0,
                15,
                0);
        //Current is used for default
        this.hrMeasureType = HeartRateMeasurementType.CURRENT;
        //1 Minute average measurement
        this.averageMeasurementTime = LocalTime.of(0,
                0,
                0);
    }

    /**
     * TimeTask Constructor, comes from user input
     *
     * @param alarmType              Type of alarm, either elapsed time or clock time
     * @param averageMeasurementTime The length of time to measure the average heart rate
     * @param time                   How long elapsed time should last or when the heart rate
     *                               measurement should be checked at a specific clock time, both
     *                               in milliseconds
     * @param maxHeartRate           Sets the max heart rate threshold, if the heart rate
     *                               measurement is above this value, the alarm will go off as
     *                               the user is awake
     * @param hrMeasureType          Type of heart rate measurement, either checks the average of
     *                               the specified average measurement time, or checks the current
     *                               heart rate when the time block is over
     */
    public TimeTask(AlarmType alarmType, LocalTime time, LocalTime averageMeasurementTime,
                    int maxHeartRate, HeartRateMeasurementType hrMeasureType) {
        this.alarmType = alarmType;
        this.averageMeasurementTime = averageMeasurementTime;
        this.time = time;
        this.maxHeartRate = maxHeartRate;
        this.hrMeasureType = hrMeasureType;
    }

    public String convertToLocalISOTime() {
        return LocalTime.parse(this.getTime().toString(),
                DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm " +
                "a"));
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
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

    public LocalTime getAverageMeasurementTime() {
        return averageMeasurementTime;
    }

    public void setAverageMeasurementTime(LocalTime averageMeasurementTime) {
        this.averageMeasurementTime = averageMeasurementTime;
    }

    public AlarmType getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmType alarmType) {
        this.alarmType = alarmType;
    }

    public static ArrayList<TimeTask> createTimeTaskList(int numOfTimeTasks) {
        ArrayList<TimeTask> timeTasks = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= numOfTimeTasks; i++) {
            int randomHour = random.ints(0,
                    23).findFirst().getAsInt();
            int randomElapsedHour = random.ints(0,
                    3).findFirst().getAsInt();
            int randomMinute = random.ints(0,
                    59).findFirst().getAsInt();
            int randomMaxHR = random.ints(50,
                    90).findFirst().getAsInt();
            int randomMinuteForAverage = random.ints(0,
                    (randomMinute == 0 ? 1 : randomMinute)).findFirst().getAsInt();
            if (i % 2 != 0) {

                timeTasks.add(new TimeTask(AlarmType.CLOCK_TIME,
                        LocalTime.of(randomHour,
                                randomMinute),
                        LocalTime.of(0,
                                0,
                                0),
                        randomMaxHR,
                        HeartRateMeasurementType.CURRENT));
            } else {

                timeTasks.add(new TimeTask(AlarmType.ELAPSED_TIME,
                        LocalTime.of(randomElapsedHour,
                                randomMinute),
                        LocalTime.of(0,
                                randomMinuteForAverage,
                                0),
                        randomMaxHR,
                        HeartRateMeasurementType.AVERAGE));

            }
        }
        return timeTasks;
    }
}
