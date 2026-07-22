package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.NeroRonnin.mdisplayaod.model.Song

@Composable
fun AlbumArt(song: Song) {

    val albumArt = song.albumArt



    if (albumArt != null) {

        Image(
            bitmap = albumArt.asImageBitmap(),
            contentDescription = "Portada de ${song.title}",
            modifier = Modifier
                .fillMaxSize()
                .blur(18.dp),
            contentScale = ContentScale.Crop
        )

    } else {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}