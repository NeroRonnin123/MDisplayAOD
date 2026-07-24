package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import com.NeroRonnin.mdisplayaod.data.MusicPreferences





@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit
) {

    val context = LocalContext.current

    val showControls =
        MusicPreferences.getShowControls(context)

    if (!showControls) {
        return
    }



    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        IconButton(
            onClick = onPrevious
        ) {
            Icon(
                imageVector = Icons.Default.FastRewind,
                contentDescription = "Anterior",
                tint = Color.White
            )
        }

        IconButton(
            onClick = onPlayPause
        ) {
            Icon(
                imageVector = if (isPlaying) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                tint = Color.White
            )
        }

        IconButton(
            onClick = onNext
        ) {
            Icon(
                imageVector = Icons.Default.FastForward,
                contentDescription = "Siguiente",
                tint = Color.White
            )
        }

    }

}