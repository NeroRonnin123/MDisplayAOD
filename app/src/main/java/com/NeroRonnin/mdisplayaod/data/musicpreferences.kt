package com.NeroRonnin.mdisplayaod.data

import android.content.Context

object MusicPreferences {

    private const val PREFS_NAME = "music_preferences"

    private const val KEY_SHOW_TITLE = "show_title"
    private const val KEY_SHOW_ARTIST = "show_artist"
    private const val KEY_SHOW_CONTROLS = "show_controls"
    private const val KEY_TITLE_SIZE = "title_size"
    private const val KEY_TITLE_WEIGHT = "title_weight"
    private const val KEY_CONTROLS_SIZE = "controls_size"

    fun getShowTitle(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_SHOW_TITLE, true)
    }

    fun setShowTitle(context: Context, value: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_SHOW_TITLE, value)
            .apply()
    }

    fun getShowArtist(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_SHOW_ARTIST, true)
    }

    fun setShowArtist(context: Context, value: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_SHOW_ARTIST, value)
            .apply()
    }

    fun getShowControls(context: Context): Boolean {
        return prefs(context).getBoolean(KEY_SHOW_CONTROLS, true)
    }

    fun setShowControls(context: Context, value: Boolean) {
        prefs(context).edit()
            .putBoolean(KEY_SHOW_CONTROLS, value)
            .apply()
    }

    fun getTitleSize(context: Context): String {
        return prefs(context).getString(KEY_TITLE_SIZE, "medium") ?: "medium"
    }

    fun setTitleSize(context: Context, value: String) {
        prefs(context).edit()
            .putString(KEY_TITLE_SIZE, value)
            .apply()
    }

    fun getControlsSize(context: Context): String {
        return prefs(context).getString(
            KEY_CONTROLS_SIZE,
            "medium"
        ) ?: "medium"
    }

    fun setControlsSize(
        context: Context,
        value: String
    ) {
        prefs(context)
            .edit()
            .putString(
                KEY_CONTROLS_SIZE,
                value
            )
            .apply()
    }


    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getTitleWeight(context: Context): String {
        return prefs(context).getString(
            KEY_TITLE_WEIGHT,
            "medium"
        ) ?: "medium"
    }

    fun setTitleWeight(
        context: Context,
        value: String
    ) {
        prefs(context)
            .edit()
            .putString(
                KEY_TITLE_WEIGHT,
                value
            )
            .apply()
    }
}