package com.NeroRonnin.mdisplayaod.viewmodel

import androidx.lifecycle.ViewModel
import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LockScreenViewModel : ViewModel() {


    private val _song = MutableStateFlow(
        Song(
            title = "Carry On My Wayward Son",
            artist = "Kansas",
            isPlaying = false
        )
    )


    val song: StateFlow<Song> = _song

}