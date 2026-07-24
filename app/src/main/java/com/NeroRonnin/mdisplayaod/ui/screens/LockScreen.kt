

package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.NeroRonnin.mdisplayaod.ui.components.AlbumArt
import com.NeroRonnin.mdisplayaod.ui.components.Clock
import com.NeroRonnin.mdisplayaod.ui.components.DateText
import com.NeroRonnin.mdisplayaod.ui.components.PlayerControls
import com.NeroRonnin.mdisplayaod.ui.components.SongInfo
import com.NeroRonnin.mdisplayaod.viewmodel.LockScreenViewModel
import android.app.Activity
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.NeroRonnin.mdisplayaod.util.ArtworkColorExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.NeroRonnin.mdisplayaod.data.MusicPreferences

@Composable
fun LockScreen() {


    val context = LocalContext.current
    val activity = context as? Activity

    val lockScreenViewModel: LockScreenViewModel = viewModel()
    val song by lockScreenViewModel.song.collectAsState()
    val hasMedia =
        song.title != "Sin reproducción"

    var automaticClockColor by remember {
        mutableStateOf(Color.White)
    }

    val showTitle =
        MusicPreferences.getShowTitle(context)

    val showArtist =
        MusicPreferences.getShowArtist(context)

    val showControls =
        MusicPreferences.getShowControls(context)

    val showSongInfo =
        showTitle || showArtist

    LaunchedEffect(song.albumArt) {

        val bitmap = song.albumArt

        automaticClockColor =
            if (bitmap != null) {

                withContext(Dispatchers.Default) {

                    Color(
                        ArtworkColorExtractor.extractColor(bitmap)
                    )
                }

            } else {

                Color.White
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {

                var totalDrag = 0f

                detectVerticalDragGestures(
                    onDragStart = {
                        totalDrag = 0f
                    },

                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        totalDrag += dragAmount
                    },

                    onDragEnd = {

                        if (totalDrag < -150f) {
                            Log.d(
                                "MDisplayAOD_GESTURE",
                                "Swipe arriba detectado"
                            )

                            activity?.finish()
                        }

                        totalDrag = 0f
                    },

                    onDragCancel = {
                        totalDrag = 0f
                    }
                )
            }
    ) {

        // CAPA 1 - Portada
        AlbumArt(song)



        // CAPA 2 - Blur
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
        )

        // CAPA 3 - Content
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Clock(
                automaticColor = automaticClockColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            DateText()

            if (hasMedia) {

                if (showSongInfo) {

                    Spacer(
                        modifier = Modifier.height(48.dp)
                    )

                    SongInfo(song)
                }

                if (showControls) {

                    Spacer(
                        modifier = Modifier.height(
                            if (showSongInfo) 32.dp
                            else 48.dp
                        )
                    )

                    PlayerControls(
                        isPlaying = song.isPlaying,
                        onPrevious = {
                            lockScreenViewModel.previous()
                        },
                        onPlayPause = {
                            lockScreenViewModel.playPause()
                        },
                        onNext = {
                            lockScreenViewModel.next()
                        }
                    )
                }
            }
        }
    }
}