package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.data.MusicPreferences
import com.NeroRonnin.mdisplayaod.model.Song
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SongInfo(
    song: Song,
    titleColor: Color = Color.White
) {

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

    val titleWeight =
        when (MusicPreferences.getTitleWeight(context)) {
            "light" -> FontWeight.Light
            "normal" -> FontWeight.Normal
            "bold" -> FontWeight.Bold
            else -> FontWeight.Medium
        }

    if (showTitle) {

        Text(
            text = song.title,
            color = titleColor,
            fontSize = titleSize,
            fontWeight = titleWeight,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }

    if (showArtist) {

        Text(
            text = song.artist,
            color = Color.Gray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
    }
}