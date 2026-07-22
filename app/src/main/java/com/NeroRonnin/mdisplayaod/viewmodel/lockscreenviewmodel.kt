package com.NeroRonnin.mdisplayaod.viewmodel

import androidx.lifecycle.ViewModel
import com.NeroRonnin.mdisplayaod.data.MusicRepository
import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LockScreenViewModel : ViewModel() {


    val song: StateFlow<Song> = MusicRepository.song

    fun playPause() {
        MusicRepository.playPause()
    }

    fun previous() {
        MusicRepository.previous()
    }

    fun next() {
        MusicRepository.next()
    }

}