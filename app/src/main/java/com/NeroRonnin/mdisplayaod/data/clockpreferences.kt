package com.NeroRonnin.mdisplayaod.data

import android.content.Context

object ClockPreferences {

    private const val PREFS_NAME = "clock_preferences"
    private const val KEY_USE_24_HOUR = "use_24_hour"
    private const val KEY_CLOCK_SIZE = "clock_size"
    private const val KEY_SHOW_DATE = "show_date"
    private const val KEY_CLOCK_COLOR = "clock_color"
    private const val KEY_CLOCK_WEIGHT = "clock_weight"


    fun getClockWeight(context: Context): String {
        return prefs(context)
            .getString(KEY_CLOCK_WEIGHT, "light")
            ?: "light"
    }

    fun setClockWeight(
        context: Context,
        value: String
    ) {
        prefs(context)
            .edit()
            .putString(KEY_CLOCK_WEIGHT, value)
            .apply()
    }

    fun getUse24Hour(context: Context): Boolean {
        return prefs(context)
            .getBoolean(KEY_USE_24_HOUR, true)
    }

    fun setUse24Hour(
        context: Context,
        value: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(KEY_USE_24_HOUR, value)
            .apply()
    }

    fun getClockSize(context: Context): String {
        return prefs(context)
            .getString(KEY_CLOCK_SIZE, "medium")
            ?: "medium"
    }

    fun setClockSize(
        context: Context,
        value: String
    ) {
        prefs(context)
            .edit()
            .putString(KEY_CLOCK_SIZE, value)
            .apply()
    }

    fun getShowDate(context: Context): Boolean {
        return prefs(context)
            .getBoolean(KEY_SHOW_DATE, true)
    }

    fun setShowDate(
        context: Context,
        value: Boolean
    ) {
        prefs(context)
            .edit()
            .putBoolean(KEY_SHOW_DATE, value)
            .apply()
    }

    fun getClockColor(context: Context): String {
        return prefs(context)
            .getString(KEY_CLOCK_COLOR, "automatic")
            ?: "automatic"
    }

    fun setClockColor(
        context: Context,
        value: String
    ) {
        prefs(context)
            .edit()
            .putString(KEY_CLOCK_COLOR, value)
            .apply()
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
}