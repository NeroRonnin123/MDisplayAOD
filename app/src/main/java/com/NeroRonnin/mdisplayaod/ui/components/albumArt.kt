package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlbumArt() {

    Box(
        modifier = Modifier
            .size(250.dp)
            .background(
                Color.DarkGray,
                RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Portada",
            color = Color.White
        )

    }

}