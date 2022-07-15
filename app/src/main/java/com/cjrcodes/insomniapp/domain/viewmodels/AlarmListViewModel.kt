package com.cjrcodes.insomniapp.domain.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cjrcodes.insomniapp.data.repositories.AlarmRepository
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm


@HiltViewModel
class AlarmListViewModel @Inject constructor(private val repository : AlarmRepository)  : ViewModel(){

    private val alarms: MutableLiveData<List<Alarm>> by lazy {
        MutableLiveData<List<Alarm>>().also {
            loadAlarms()
        }
    }

    fun getAlarms(): LiveData<List<Alarm>> {
        return alarms
    }

    private fun loadAlarms() {
        // Do an asynchronous operation to fetch alarms.
    }
}