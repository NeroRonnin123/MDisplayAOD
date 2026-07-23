package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ClockSettingsScreen(
    onBack: () -> Unit
) {

    BackHandler {
        onBack()
    }

    val background = Color(0xFF08090E)
    val primaryText = Color(0xFFF1F1F5)
    val secondaryText = Color(0xFF9B9BA5)
    val purple = Color(0xFF7C5CFF)

    var use24Hour by remember {
        mutableStateOf(true)
    }

    var selectedColor by remember {
        mutableStateOf("automatic")
    }

    var currentTime by remember {
        mutableStateOf(Date())
    }

    LaunchedEffect(Unit) {

        while (true) {

            currentTime = Date()

            delay(1000)
        }
    }


    val timeFormat =
        if (use24Hour) {
            "HH:mm"
        } else {
            "hh:mm"
        }

    val formattedTime =
        remember(currentTime, use24Hour) {
            SimpleDateFormat(
                timeFormat,
                Locale.getDefault()
            ).format(currentTime)
        }

    val clockColor =
        when (selectedColor) {

            "white" -> Color.White
            "purple" -> Color(0xFF8268FF)
            "blue" -> Color(0xFF5C7CFA)
            "green" -> Color(0xFF62D482)
            "red" -> Color(0xFFFF6464)

            // Temporal.
            // Después vendrá de la portada.
            else -> Color(0xFF8268FF)
        }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
    ) {

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
            modifier = Modifier.height(40.dp)
        )

        Text(
            text = "VISTA PREVIA",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Text(
            text = "FORMATO",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                        text = if (use24Hour) {
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
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "COLOR",
            color = purple,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

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
                }
            )

            ColorOption(
                color = Color.White,
                selected = selectedColor == "white",
                onClick = {
                    selectedColor = "white"
                }
            )

            ColorOption(
                color = Color(0xFF8268FF),
                selected = selectedColor == "purple",
                onClick = {
                    selectedColor = "purple"
                }
            )

            ColorOption(
                color = Color(0xFF5C7CFA),
                selected = selectedColor == "blue",
                onClick = {
                    selectedColor = "blue"
                }
            )

            ColorOption(
                color = Color(0xFF62D482),
                selected = selectedColor == "green",
                onClick = {
                    selectedColor = "green"
                }
            )

            ColorOption(
                color = Color(0xFFFF6464),
                selected = selectedColor == "red",
                onClick = {
                    selectedColor = "red"
                }
            )
        }


        // Temporal. Después aquí pondremos
        // la previsualización real del reloj.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = formattedTime,
                color = clockColor,
                fontSize = 64.sp,
                fontWeight = FontWeight.Light
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
                if (selected) 42.dp else 36.dp
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