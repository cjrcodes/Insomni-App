package com.cjrcodes.insomniapp.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cjrcodes.insomniapp.data.daos.AlarmDao
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImplementation(private val alarmDao: AlarmDao) : AlarmRepository
{
    private val searchResults = MutableLiveData<List<Alarm>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms()

    override suspend fun createAlarm(alarm: Alarm) {
        coroutineScope.launch(Dispatchers.IO) {
            alarmDao.insertAlarm(alarm)
        }
    }

    override suspend fun deleteAlarm(alarm : Alarm) {
        coroutineScope.launch(Dispatchers.IO) {

            alarmDao.deleteAlarm(alarm)
        }
    }

    override suspend fun getAlarmById(alarmId: Int) {
        coroutineScope.launch(Dispatchers.Main) {
            searchResults.value = asyncFind(alarmId).await()
        }
    }

    private fun asyncFind(alarmId: Int): Deferred<List<Alarm>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async alarmDao.getAlarmById(alarmId)
        }


}