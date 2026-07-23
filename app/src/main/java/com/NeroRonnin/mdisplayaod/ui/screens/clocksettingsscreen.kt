package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import com.NeroRonnin.mdisplayaod.data.ClockPreferences

@Composable
fun ClockSettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current

    BackHandler {
        onBack()
    }

    // Colores de la interfaz
    val background = Color(0xFF08090E)
    val primaryText = Color(0xFFF1F1F5)
    val secondaryText = Color(0xFF9B9BA5)
    val purple = Color(0xFF7C5CFF)

    // Configuración temporal del reloj
    var use24Hour by remember {
        mutableStateOf(
            ClockPreferences.getUse24Hour(context)
        )
    }

    var selectedColor by remember {
        mutableStateOf(
            ClockPreferences.getClockColor(context)
        )
    }

    var clockSize by remember {
        mutableStateOf(
            ClockPreferences.getClockSize(context)
        )
    }

    var clockWeight by remember {
        mutableStateOf(
            ClockPreferences.getClockWeight(context)
        )
    }

    var showDate by remember {
        mutableStateOf(
            ClockPreferences.getShowDate(context)
        )
    }

    // Hora actual
    var currentTime by remember {
        mutableStateOf(Date())
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    // Formato de hora
    val timeFormat =
        if (use24Hour) {
            "HH:mm"
        } else {
            "h:mm a"
        }

    val formattedTime =
        remember(currentTime, use24Hour) {
            SimpleDateFormat(
                timeFormat,
                Locale.getDefault()
            ).format(currentTime)
        }

    // Color de reloj
    val clockColor =
        when (selectedColor) {

            "white" -> Color.White
            "purple" -> Color(0xFF8268FF)
            "blue" -> Color(0xFF5C7CFA)
            "green" -> Color(0xFF62D482)
            "red" -> Color(0xFFFF6464)

            // Temporal:
            // después vendrá de la portada.
            else -> Color(0xFF8268FF)
        }

    val clockFontSize =
        when (clockSize) {
            "small" -> 48.sp
            "large" -> 80.sp
            else -> 64.sp
        }


    val clockFontWeight =
        when (clockWeight) {
            "normal" -> FontWeight.Normal
            "bold" -> FontWeight.Bold
            else -> FontWeight.Light
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 40.dp
            )
    ) {

        // HEADER
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = primaryText
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Column {

                Text(
                    text = "Reloj",
                    color = primaryText,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Personaliza cómo se muestra la hora",
                    color = secondaryText,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        // =========================
        // VISTA PREVIA
        // =========================

        Text(
            text = "VISTA PREVIA",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                1.dp,
                Color(0xFF272B35)
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF12151C)
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = formattedTime,
                        color = clockColor,
                        fontSize = clockFontSize,
                        fontWeight = clockFontWeight
                    )

                    if (showDate) {

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = SimpleDateFormat(
                                "EEEE, d 'de' MMMM",
                                Locale.getDefault()
                            ).format(currentTime),
                            color = secondaryText,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =========================
        // FORMATO
        // =========================

        Text(
            text = "FORMATO",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF12151C)
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFF272B35)
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Formato de 24 horas",
                        color = primaryText,
                        fontSize = 16.sp
                    )

                    Text(
                        text =
                            if (use24Hour) {
                                "Ejemplo: 18:45"
                            } else {
                                "Ejemplo: 06:45 PM"
                            },
                        color = secondaryText,
                        fontSize = 12.sp
                    )
                }

                Switch(
                    checked = use24Hour,
                    onCheckedChange = {

                        use24Hour = it

                        ClockPreferences.setUse24Hour(
                            context,
                            it
                        )
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =========================
        // TAMAÑO
        // =========================

        Text(
            text = "TAMAÑO",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Pequeño",
                selected = clockSize == "small",
                onClick = {
                    clockSize = "small"
                    ClockPreferences.setClockSize(
                        context,
                        "small"
                    )
                }
            )

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Mediano",
                selected = clockSize == "medium",
                onClick = {
                    clockSize = "medium"
                    ClockPreferences.setClockSize(
                        context,
                        "medium"
                    )
                }
            )

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Grande",
                selected = clockSize == "large",
                onClick = {
                    clockSize = "large"
                    ClockPreferences.setClockSize(
                        context,
                        "large"
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
        // =========================
        // Bold
        // =========================

        Text(
            text = "GROSOR",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Ligero",
                selected = clockWeight == "light",
                onClick = {
                    clockWeight = "light"

                    ClockPreferences.setClockWeight(
                        context,
                        "light"
                    )
                }
            )

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Normal",
                selected = clockWeight == "normal",
                onClick = {
                    clockWeight = "normal"

                    ClockPreferences.setClockWeight(
                        context,
                        "normal"
                    )
                }
            )

            ClockSizeOption(
                modifier = Modifier.weight(1f),
                text = "Negrita",
                selected = clockWeight == "bold",
                onClick = {
                    clockWeight = "bold"

                    ClockPreferences.setClockWeight(
                        context,
                        "bold"
                    )
                }
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        // =========================
        // FECHA
        // =========================

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF12151C)
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFF272B35)
            )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Mostrar fecha",
                        color = primaryText,
                        fontSize = 16.sp
                    )

                    Text(
                        text = "Muestra la fecha debajo de la hora",
                        color = secondaryText,
                        fontSize = 12.sp
                    )
                }

                Switch(
                    checked = showDate,
                    onCheckedChange = { checked ->

                        showDate = checked

                        ClockPreferences.setShowDate(
                            context,
                            checked
                        )
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        // =========================
        // COLOR
        // =========================

        Text(
            text = "COLOR",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ColorOption(
                color = Color(0xFF8268FF),
                selected = selectedColor == "automatic",
                onClick = {
                    selectedColor = "automatic"
                    ClockPreferences.setClockColor(
                        context,
                        "automatic"
                    )
                }
            )

            ColorOption(
                color = Color.White,
                selected = selectedColor == "white",
                onClick = {
                    selectedColor = "white"
                    ClockPreferences.setClockColor(
                        context,
                        "white"
                    )
                }
            )

            ColorOption(
                color = Color(0xFF8268FF),
                selected = selectedColor == "purple",
                onClick = {
                    selectedColor = "purple"
                    ClockPreferences.setClockColor(
                        context,
                        "purple"
                    )
                }
            )

            ColorOption(
                color = Color(0xFF5C7CFA),
                selected = selectedColor == "blue",
                onClick = {
                    selectedColor = "blue"
                    ClockPreferences.setClockColor(
                        context,
                        "blue"
                    )
                }
            )

            ColorOption(
                color = Color(0xFF62D482),
                selected = selectedColor == "green",
                onClick = {
                    selectedColor = "green"
                    ClockPreferences.setClockColor(
                        context,
                        "green"
                    )
                }
            )

            ColorOption(
                color = Color(0xFFFF6464),
                selected = selectedColor == "red",
                onClick = {
                    selectedColor = "red"
                    ClockPreferences.setClockColor(
                        context,
                        "red"
                    )
                }
            )
        }
    }
}

@Composable
private fun ClockSizeOption(
    modifier: Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            if (selected) {
                Color(0xFF8268FF)
            } else {
                Color(0xFF272B35)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    Color(0xFF8268FF)
                        .copy(alpha = 0.12f)
                } else {
                    Color(0xFF12151C)
                }
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                color =
                    if (selected) {
                        Color(0xFF8268FF)
                    } else {
                        Color(0xFFF1F1F5)
                    },
                fontSize = 13.sp,
                fontWeight =
                    if (selected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    }
            )
        }
    }
}

@Composable
private fun ColorOption(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(
                if (selected) {
                    42.dp
                } else {
                    36.dp
                }
            )
            .clip(CircleShape)
            .background(color)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {

        if (selected) {

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (color == Color.White) {
                            Color.Black
                        } else {
                            Color.White
                        }
                    )
            )
        }
    }
}