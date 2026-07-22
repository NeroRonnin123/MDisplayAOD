

package com.neroronnin.mdisplayaod.ui.screens

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


@Composable
fun LockScreen() {
    val lockScreenViewModel: LockScreenViewModel = viewModel()

    val song by lockScreenViewModel.song.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Clock()

            Spacer(modifier = Modifier.height(8.dp))

            DateText()

            Spacer(modifier = Modifier.height(48.dp))

            AlbumArt()

            Spacer(modifier = Modifier.height(24.dp))

            SongInfo(song)

            Spacer(modifier = Modifier.height(32.dp))

            PlayerControls()
        }

    }

}