package com.NeroRonnin.mdisplayaod.data

import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MusicRepository {

    private val _song = MutableStateFlow(Song())

    val song: StateFlow<Song> = _song
    private var onPlayPause: (() -> Unit)? = null
    private var onPrevious: (() -> Unit)? = null
    private var onNext: (() -> Unit)? = null

    fun updateSong(song: Song) {
        _song.value = song
    }


    fun setPlayPauseAction(action: () -> Unit) {
        onPlayPause = action
    }

    fun playPause() {
        onPlayPause?.invoke()
    }


    fun setPreviousAction(action: () -> Unit) {
        onPrevious = action
    }

    fun setNextAction(action: () -> Unit) {
        onNext = action
    }

    fun previous() {
        onPrevious?.invoke()
    }

    fun next() {
        onNext?.invoke()
    }
}