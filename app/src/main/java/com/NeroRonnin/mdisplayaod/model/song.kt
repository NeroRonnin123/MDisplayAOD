package com.NeroRonnin.mdisplayaod.model

import android.graphics.Bitmap

data class Song(
    val title: String = "Sin reproducción",
    val artist: String = "",
    val albumArt: Bitmap? = null,
    val isPlaying: Boolean = false
)