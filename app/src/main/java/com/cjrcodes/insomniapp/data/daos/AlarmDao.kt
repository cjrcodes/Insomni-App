package com.cjrcodes.insomniapp.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms WHERE alarmId = :id")
    fun getAlarmById(id: Int): List<Alarm>

    @Delete
    fun deleteAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<Alarm>>
}