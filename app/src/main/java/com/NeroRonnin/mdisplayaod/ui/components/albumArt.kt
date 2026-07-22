package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.NeroRonnin.mdisplayaod.R

@Composable
fun AlbumArt() {
    Image(
        painter = painterResource(R.drawable.album_test),
        contentDescription = "Portada de la canción",
        modifier = Modifier
            .fillMaxSize()
            .blur(18.dp),
        contentScale = ContentScale.Crop
    )
}