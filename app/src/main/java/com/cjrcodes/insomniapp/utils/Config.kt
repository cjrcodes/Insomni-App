package com.cjrcodes.insomniapp.utils

import android.content.Context
import android.text.format.DateFormat

class Config {
    companion object {
        private var twelveHourFormat: Boolean = true

        fun getTwelveHourFormat(): Boolean {
            return twelveHourFormat
        }

        fun setTwelveHourFormat(context: Context): Boolean{
            twelveHourFormat = !DateFormat.is24HourFormat(context)
            return twelveHourFormat
        }
    }

}