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

@Composable
fun PlayerControls() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        Icon(Icons.Default.FastRewind, null, tint = Color.White)

        Icon(Icons.Default.PlayArrow, null, tint = Color.White)

        Icon(Icons.Default.FastForward, null, tint = Color.White)

    }

}