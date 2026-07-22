package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.model.Song

@Composable
fun SongInfo(song: Song) {




    Text(
        text = song.title,
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium
    )

    Text(
        text = song.artist,
        color = Color.Gray,
        fontSize = 18.sp
    )

}