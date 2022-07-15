package com.cjrcodes.insomniapp.data.repositories

import com.cjrcodes.insomniapp.data.daos.AlarmDao
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun createAlarm(alarm : Alarm)

    suspend fun deleteAlarm(alarm : Alarm)

    suspend fun getAlarmById(alarmId : Int)

}