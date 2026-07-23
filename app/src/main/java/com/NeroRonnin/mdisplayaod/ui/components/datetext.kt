package com.NeroRonnin.mdisplayaod.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.data.ClockPreferences
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateText() {

    val context = LocalContext.current

    val showDate =
        ClockPreferences.getShowDate(context)

    // Si el usuario desactivó la fecha,
    // este componente no dibuja nada.
    if (!showDate) {
        return
    }

    var date by remember {
        mutableStateOf(LocalDate.now())
    }

    LaunchedEffect(Unit) {
        while (true) {
            date = LocalDate.now()
            delay(60000)
        }
    }

    Text(
        text = date.format(
            DateTimeFormatter.ofPattern(
                "EEEE d 'de' MMMM",
                Locale("es", "MX")
            )
        ).replaceFirstChar {
            it.uppercase()
        },
        color = Color.Gray,
        fontSize = 18.sp
    )
}