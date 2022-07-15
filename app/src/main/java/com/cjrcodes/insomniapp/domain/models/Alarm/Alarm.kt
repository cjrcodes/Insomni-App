package com.cjrcodes.insomniapp.domain.models.Alarm

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime
import kotlin.random.Random

/**
 * @author Christian Rodriguez
 * TimeTask class, an instance of this class contains all information needed to start a timed
 * session of tracking
 */

/**
 * An Alarm entity class with an auto-generated [id], this is the primary entity class for the project.
 * The [time] specifies either the length of the elapsed timer or specified clock time, in milliseconds.
 * [maxHeartRate] sets the max heart rate threshold, if the average heart rate measurement is above this value, the alarm will go off as the user is considered awake.
 * The [alarmType] specifies whether the alarm is elapsed or clock time based, as that is how the [time] variable will be interpreted.
 * [hasVibration] is whether the alarm will vibrate if it is set to alert the user if they are considered awake.
 * */
@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "alarmId") val id: Int = 0,
    @ColumnInfo(name = "time") val time: LocalTime = LocalTime.MIN,
    @ColumnInfo(name = "max_heart_rate") val maxHeartRate: Int = 70,
    @ColumnInfo(name = "alarm_type") val alarmType: AlarmType = AlarmType.ELAPSED_TIME,
    @ColumnInfo(name = "vibrates") val hasVibration: Boolean? = true
) {

    //Empty constructor for Compose @Preview testing purposes
    constructor() : this(Random.nextInt(), LocalTime.MIN, 70, AlarmType.ELAPSED_TIME, true)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Alarm

        if (id != other.id) return false
        if (time != other.time) return false
        if (maxHeartRate != other.maxHeartRate) return false
        if (alarmType != other.alarmType) return false
        if (hasVibration != other.hasVibration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + time.hashCode()
        result = 31 * result + maxHeartRate
        result = 31 * result + alarmType.hashCode()
        result = 31 * result + (hasVibration?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Alarm(id=$id, time=$time, maxHeartRate=$maxHeartRate, alarmType=$alarmType, hasVibration=$hasVibration)"
    }


}

enum class AlarmType {
    CLOCK_TIME, ELAPSED_TIME
}
