package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun Clock() {

    var time by remember {
        mutableStateOf(LocalTime.now())
    }

    LaunchedEffect(Unit) {

        while (true) {

            time = LocalTime.now()

            delay(1000)

        }

    }

    Text(
        text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
        color = Color.White,
        fontSize = 72.sp,
        fontWeight = FontWeight.Light
    )

}