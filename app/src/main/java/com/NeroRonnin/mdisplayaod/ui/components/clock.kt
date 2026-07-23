package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.data.ClockPreferences
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun Clock(
    automaticColor: Color = Color.White
) {

    val context = LocalContext.current

    val use24Hour =
        ClockPreferences.getUse24Hour(context)

    val clockSize =
        ClockPreferences.getClockSize(context)

    val clockWeight =
        ClockPreferences.getClockWeight(context)


    val fontWeight =
        when (clockWeight) {
            "normal" -> FontWeight.Normal
            "bold" -> FontWeight.Bold
            else -> FontWeight.Light
        }

    val selectedColor =
        ClockPreferences.getClockColor(context)

    var time by remember {
        mutableStateOf(LocalTime.now())
    }

    LaunchedEffect(Unit) {
        while (true) {
            time = LocalTime.now()
            delay(1000)
        }
    }

    val timePattern =
        if (use24Hour) {
            "HH:mm"
        } else {
            "h:mm a"
        }

    val fontSize =
        when (clockSize) {
            "small" -> 48.sp
            "large" -> 80.sp
            else -> 64.sp
        }

    val clockColor =
        when (selectedColor) {

            "white" -> Color.White
            "purple" -> Color(0xFF8268FF)
            "blue" -> Color(0xFF5C7CFA)
            "green" -> Color(0xFF62D482)
            "red" -> Color(0xFFFF6464)

            "automatic",
            "auto" -> automaticColor

            else -> automaticColor
        }

    Text(
        text = time.format(
            DateTimeFormatter.ofPattern(timePattern)
        ),
        color = clockColor,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}