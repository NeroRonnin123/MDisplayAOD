package com.NeroRonnin.mdisplayaod.model

data class Song(
    val title: String = "Sin reproducción",
    val artist: String = "",
    val albumArt: String? = null,
    val isPlaying: Boolean = false
)