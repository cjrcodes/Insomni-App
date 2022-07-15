package com.cjrcodes.insomniapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cjrcodes.insomniapp.data.daos.AlarmDao
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm

@Database(entities = [(Alarm::class)], version = 1)
abstract class AlarmRoomDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {

        private var INSTANCE: AlarmRoomDatabase? = null

        fun getInstance(context: Context): AlarmRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlarmRoomDatabase::class.java,
                        "alarm_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}