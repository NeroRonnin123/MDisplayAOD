package com.NeroRonnin.mdisplayaod.data

import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MusicRepository {

    private val _song = MutableStateFlow(Song())

    val song: StateFlow<Song> = _song

    fun updateSong(song: Song) {
        _song.value = song
    }
}