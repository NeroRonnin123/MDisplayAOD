package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.data.MusicPreferences
import com.NeroRonnin.mdisplayaod.model.Song

@Composable
fun SongInfo(song: Song) {

    val context = LocalContext.current

    val showTitle =
        MusicPreferences.getShowTitle(context)

    val showArtist =
        MusicPreferences.getShowArtist(context)

    val titleSize =
        when (MusicPreferences.getTitleSize(context)) {
            "small" -> 18.sp
            "large" -> 30.sp
            else -> 24.sp
        }

    if (showTitle) {

        Text(
            text = song.title,
            color = Color.White,
            fontSize = titleSize,
            fontWeight = FontWeight.Medium
        )
    }

    if (showArtist) {

        Text(
            text = song.artist,
            color = Color.Gray,
            fontSize = 18.sp
        )
    }
}