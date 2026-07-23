

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

@Composable
fun LockScreen() {


    val context = LocalContext.current
    val activity = context as? Activity

    val lockScreenViewModel: LockScreenViewModel = viewModel()
    val song by lockScreenViewModel.song.collectAsState()

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

                            activity?.moveTaskToBack(true)
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

            Clock()

            Spacer(modifier = Modifier.height(8.dp))

            DateText()

            Spacer(modifier = Modifier.height(48.dp))

            SongInfo(song)

            Spacer(modifier = Modifier.height(32.dp))

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