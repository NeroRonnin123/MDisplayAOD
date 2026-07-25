package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.NeroRonnin.mdisplayaod.data.MusicPreferences

private val Background = Color(0xFF07080D)
private val CardBackground = Color(0xFF12151C)
private val CardBorder = Color(0xFF292D38)

private val Purple = Color(0xFF7C5CFF)
private val PurpleBackground = Color(0xFF211A38)

private val PrimaryText = Color(0xFFF2F2F5)
private val SecondaryText = Color(0xFF9A9AA3)

@Composable
fun MusicSettingsScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var showTitle by remember {
        mutableStateOf(MusicPreferences.getShowTitle(context))
    }

    var showArtist by remember {
        mutableStateOf(MusicPreferences.getShowArtist(context))
    }

    var showControls by remember {
        mutableStateOf(MusicPreferences.getShowControls(context))
    }

    var titleSize by remember {
        mutableStateOf(MusicPreferences.getTitleSize(context))
    }

    var titleWeight by remember {
        mutableStateOf(MusicPreferences.getTitleWeight(context))
    }

    var controlsSize by remember {
        mutableStateOf(MusicPreferences.getControlsSize(context))
    }

    // CONTENEDOR GENERAL
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {

        // =========================================================
        // ZONA FIJA
        // Header + Preview
        // =========================================================

        Column(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp,
                top = 24.dp
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
                        tint = PrimaryText
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Column {

                    Text(
                        text = "Música",
                        color = PrimaryText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Light
                    )

                    Text(
                        text = "Personaliza la información de reproducción",
                        color = SecondaryText,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            SectionTitle("VISTA PREVIA")

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            MusicPreview(
                showTitle = showTitle,
                showArtist = showArtist,
                showControls = showControls,
                titleSize = titleSize,
                titleWeight = titleWeight,
                controlsSize = controlsSize
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }

        // =========================================================
        // ZONA CON SCROLL
        // =========================================================

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 40.dp
                )
        ) {

            // =====================================================
            // INFORMACIÓN
            // =====================================================

            SectionTitle("INFORMACIÓN")

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MusicSwitchCard(
                title = "Mostrar título",
                description = "Muestra el nombre de la canción",
                checked = showTitle,
                onCheckedChange = {

                    showTitle = it

                    MusicPreferences.setShowTitle(
                        context,
                        it
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            MusicSwitchCard(
                title = "Mostrar artista",
                description = "Muestra el artista debajo del título",
                checked = showArtist,
                onCheckedChange = {

                    showArtist = it

                    MusicPreferences.setShowArtist(
                        context,
                        it
                    )
                }
            )

            // =====================================================
            // TAMAÑO DEL TÍTULO
            // =====================================================

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            SectionTitle("TAMAÑO DEL TÍTULO")

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                SizeOption(
                    modifier = Modifier.weight(1f),
                    text = "Pequeño",
                    selected = titleSize == "small"
                ) {
                    titleSize = "small"

                    MusicPreferences.setTitleSize(
                        context,
                        "small"
                    )
                }

                SizeOption(
                    modifier = Modifier.weight(1f),
                    text = "Mediano",
                    selected = titleSize == "medium"
                ) {
                    titleSize = "medium"

                    MusicPreferences.setTitleSize(
                        context,
                        "medium"
                    )
                }

                SizeOption(
                    modifier = Modifier.weight(1f),
                    text = "Grande",
                    selected = titleSize == "large"
                ) {
                    titleSize = "large"

                    MusicPreferences.setTitleSize(
                        context,
                        "large"
                    )
                }
            }

            // =====================================================
            // GROSOR DEL TÍTULO
            // =====================================================

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            SectionTitle("GROSOR DEL TÍTULO")

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                WeightOption(
                    modifier = Modifier.weight(1f),
                    text = "Light",
                    selected = titleWeight == "light"
                ) {
                    titleWeight = "light"

                    MusicPreferences.setTitleWeight(
                        context,
                        "light"
                    )
                }

                WeightOption(
                    modifier = Modifier.weight(1f),
                    text = "Normal",
                    selected = titleWeight == "normal"
                ) {
                    titleWeight = "normal"

                    MusicPreferences.setTitleWeight(
                        context,
                        "normal"
                    )
                }

                WeightOption(
                    modifier = Modifier.weight(1f),
                    text = "Medium",
                    selected = titleWeight == "medium"
                ) {
                    titleWeight = "medium"

                    MusicPreferences.setTitleWeight(
                        context,
                        "medium"
                    )
                }

                WeightOption(
                    modifier = Modifier.weight(1f),
                    text = "Bold",
                    selected = titleWeight == "bold"
                ) {
                    titleWeight = "bold"

                    MusicPreferences.setTitleWeight(
                        context,
                        "bold"
                    )
                }
            }

            // =====================================================
            // CONTROLES
            // =====================================================

            Spacer(
                modifier = Modifier.height(34.dp)
            )

            SectionTitle("CONTROLES")

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            MusicSwitchCard(
                title = "Mostrar controles",
                description = "Anterior, reproducir y siguiente",
                checked = showControls,
                onCheckedChange = {

                    showControls = it

                    MusicPreferences.setShowControls(
                        context,
                        it
                    )
                }
            )

            // Tamaño de controles.
            // Solo se muestra si los controles están habilitados.
            if (showControls) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    SizeOption(
                        modifier = Modifier.weight(1f),
                        text = "Pequeño",
                        selected = controlsSize == "small"
                    ) {
                        controlsSize = "small"

                        MusicPreferences.setControlsSize(
                            context,
                            "small"
                        )
                    }

                    SizeOption(
                        modifier = Modifier.weight(1f),
                        text = "Mediano",
                        selected = controlsSize == "medium"
                    ) {
                        controlsSize = "medium"

                        MusicPreferences.setControlsSize(
                            context,
                            "medium"
                        )
                    }

                    SizeOption(
                        modifier = Modifier.weight(1f),
                        text = "Grande",
                        selected = controlsSize == "large"
                    ) {
                        controlsSize = "large"

                        MusicPreferences.setControlsSize(
                            context,
                            "large"
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )
        }
    }
}


// =============================================================
// PREVIEW
// =============================================================

@Composable
private fun MusicPreview(
    showTitle: Boolean,
    showArtist: Boolean,
    showControls: Boolean,
    titleSize: String,
    titleWeight: String,
    controlsSize: String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            1.dp,
            CardBorder
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (showTitle) {

                Text(
                    text = "The Hype - Berlin",
                    color = PrimaryText,
                    fontSize = when (titleSize) {
                        "small" -> 16.sp
                        "large" -> 24.sp
                        else -> 20.sp
                    },
                    fontWeight = when (titleWeight) {
                        "light" -> FontWeight.Light
                        "normal" -> FontWeight.Normal
                        "bold" -> FontWeight.Bold
                        else -> FontWeight.Medium
                    }
                )
            }

            if (showArtist) {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Twenty One Pilots",
                    color = SecondaryText,
                    fontSize = 14.sp
                )
            }

            if (showControls) {

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                val controlIconSize =
                    when (controlsSize) {

                        "small" -> 20.dp

                        "large" -> 34.dp

                        else -> 26.dp
                    }

                Row(
                    modifier = Modifier.fillMaxWidth(0.65f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.FastRewind,
                        contentDescription = null,
                        tint = PrimaryText,
                        modifier = Modifier.size(controlIconSize)
                    )

                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = null,
                        tint = PrimaryText,
                        modifier = Modifier.size(controlIconSize)
                    )

                    Icon(
                        imageVector = Icons.Default.FastForward,
                        contentDescription = null,
                        tint = PrimaryText,
                        modifier = Modifier.size(controlIconSize)
                    )
                }
            }
        }
    }
}


// =============================================================
// SWITCH CARD
// =============================================================

@Composable
private fun MusicSwitchCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            1.dp,
            CardBorder
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = PrimaryText,
                    fontSize = 16.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = description,
                    color = SecondaryText,
                    fontSize = 13.sp
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}


// =============================================================
// SIZE OPTION
// =============================================================

@Composable
private fun SizeOption(
    modifier: Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(58.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width =
                if (selected) {
                    2.dp
                } else {
                    1.dp
                },
            color =
                if (selected) {
                    Purple
                } else {
                    CardBorder
                }
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    PurpleBackground
                } else {
                    CardBackground
                }
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                color =
                    if (selected) {
                        Purple
                    } else {
                        PrimaryText
                    },
                fontSize = 14.sp
            )
        }
    }
}


// =============================================================
// WEIGHT OPTION
// =============================================================

@Composable
private fun WeightOption(
    modifier: Modifier,
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .height(58.dp)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width =
                if (selected) {
                    2.dp
                } else {
                    1.dp
                },
            color =
                if (selected) {
                    Purple
                } else {
                    CardBorder
                }
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    PurpleBackground
                } else {
                    CardBackground
                }
        )
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                color =
                    if (selected) {
                        Purple
                    } else {
                        PrimaryText
                    },
                fontSize = 12.sp,
                fontWeight =
                    when (text) {

                        "Light" ->
                            FontWeight.Light

                        "Normal" ->
                            FontWeight.Normal

                        "Bold" ->
                            FontWeight.Bold

                        else ->
                            FontWeight.Medium
                    }
            )
        }
    }
}


// =============================================================
// SECTION TITLE
// =============================================================

@Composable
private fun SectionTitle(
    text: String
) {

    Text(
        text = text,
        color = Purple,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
}