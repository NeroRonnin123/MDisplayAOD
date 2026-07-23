package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.text.style.TextOverflow

private val Background = Color(0xFF080A0F)
private val CardBackground = Color(0xFF12151C)
private val CardBorder = Color(0xFF272B35)

private val Purple = Color(0xFF8268FF)
private val Blue = Color(0xFF536DFE)

private val Success = Color(0xFF62D482)
private val WarningColor = Color(0xFFFFBE45)

private val PrimaryText = Color(0xFFF4F4F6)
private val SecondaryText = Color(0xFFA6A7AF)

@Composable
fun SettingsScreen(
    isEnabled: Boolean,
    overlayGranted: Boolean,
    notificationAccessGranted: Boolean,
    postNotificationsGranted: Boolean,
    batteryOptimizationDisabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onOverlayClick: () -> Unit,
    onNotificationAccessClick: () -> Unit,
    onPostNotificationsClick: () -> Unit,
    onBatteryOptimizationClick: () -> Unit,
    onPreviewClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 24.dp
            )
    ) {

        // HEADER

        Text(
            text = "MDisplayAOD",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Purple
        )

        Text(
            text = "Panel de administración",
            fontSize = 16.sp,
            color = SecondaryText
        )

        Spacer(modifier = Modifier.height(28.dp))

        // ESTADO PRINCIPAL

        MainStatusCard(
            isEnabled = isEnabled,
            onEnabledChange = onEnabledChange
        )

        Spacer(modifier = Modifier.height(30.dp))

        SectionTitle("PERMISOS")

        Spacer(modifier = Modifier.height(12.dp))

        SettingsCard {

            PermissionItem(
                icon = Icons.Default.Layers,
                title = "Mostrar sobre otras apps",
                description = "Permite que MDisplayAOD se muestre encima de otras aplicaciones",
                granted = overlayGranted,
                onClick = onOverlayClick
            )

            PermissionItem(
                icon = Icons.Default.Notifications,
                title = "Acceso a notificaciones",
                description = "Permite leer la información de reproducción",
                granted = notificationAccessGranted,
                onClick = onNotificationAccessClick
            )

            PermissionItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones de la aplicación",
                description = "Permite recibir notificaciones importantes",
                granted = postNotificationsGranted,
                onClick = onPostNotificationsClick
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        SectionTitle("SISTEMA")

        Spacer(modifier = Modifier.height(12.dp))

        SettingsCard {

            BatteryItem(
                granted = batteryOptimizationDisabled,
                onClick = onBatteryOptimizationClick
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        SectionTitle("PERSONALIZACIÓN")

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomizationCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.AccessTime,
                title = "Reloj",
                description = "Estilo y color"
            )

            CustomizationCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.MusicNote,
                title = "Música",
                description = "Info y controles"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomizationCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Image,
                title = "Fondo",
                description = "Portadas y blur"
            )

            CustomizationCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Settings,
                title = "Gestos",
                description = "Pantalla y acciones"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PreviewButton(
            onClick = onPreviewClick
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MainStatusCard(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(
            1.dp,
            Purple.copy(alpha = 0.55f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Purple.copy(alpha = 0.12f),
                            Blue.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            StatusIcon(
                enabled = isEnabled
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = if (isEnabled) {
                        "MDisplayAOD activo"
                    } else {
                        "MDisplayAOD desactivado"
                    },
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryText
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (isEnabled) {
                        "Tu pantalla personalizada está lista para usarse"
                    } else {
                        "Actívalo para usar tu pantalla personalizada"
                    },
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Switch(
                checked = isEnabled,
                onCheckedChange = onEnabledChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Purple,
                    uncheckedThumbColor = SecondaryText,
                    uncheckedTrackColor = CardBorder
                )
            )
        }
    }
}

@Composable
private fun StatusIcon(
    enabled: Boolean
) {

    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                if (enabled) {
                    Purple.copy(alpha = 0.15f)
                } else {
                    Color.White.copy(alpha = 0.05f)
                },
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = if (enabled) {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Warning
            },
            contentDescription = null,
            tint = if (enabled) Purple else WarningColor,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
private fun SectionTitle(
    title: String
) {

    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Purple
    )
}

@Composable
private fun SettingsCard(
    content: @Composable () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp,
            CardBorder
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {

        Column {
            content()
        }
    }
}

@Composable
private fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    granted: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!granted) {
                    onClick()
                }
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingIcon(
            icon = icon,
            tint = Purple,
            size = 40.dp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                fontSize = 16.sp,
                color = PrimaryText
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = SecondaryText
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = if (granted) {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Warning
            },
            contentDescription = null,
            tint = if (granted) {
                Success
            } else {
                WarningColor
            },
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = if (granted) {
                "Concedido"
            } else {
                "Configurar"
            },
            fontSize = 13.sp,
            color = if (granted) {
                Success
            } else {
                WarningColor
            }
        )

        if (!granted) {

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SecondaryText
            )
        }
    }
}

@Composable
private fun BatteryItem(
    granted: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!granted) {
                    onClick()
                }
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        SettingIcon(
            icon = Icons.Default.BatteryAlert,
            tint = WarningColor,
            size = 40.dp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Optimización de batería",
                fontSize = 15.sp,
                color = PrimaryText,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Evita restricciones en segundo plano",
                fontSize = 12.sp,
                color = SecondaryText
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = if (granted) {
                Icons.Default.CheckCircle
            } else {
                Icons.Default.Warning
            },
            contentDescription = null,
            tint = if (granted) Success else WarningColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = if (granted) {
                "Correcto"
            } else {
                "Configurar"
            },
            fontSize = 13.sp,
            color = if (granted) {
                Success
            } else {
                WarningColor
            }
        )

        if (!granted) {

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SecondaryText
            )
        }
    }
}

@Composable
private fun SettingIcon(
    icon: ImageVector,
    tint: Color,
    size: Dp = 44.dp
) {

    Box(
        modifier = Modifier
            .size(size)
            .background(
                tint.copy(alpha = 0.12f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun CustomizationCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    description: String
) {

    Card(
        modifier = modifier,
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
                .padding(
                    horizontal = 12.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SettingIcon(
                icon = icon,
                tint = Purple,
                size = 38.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = SecondaryText
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = SecondaryText
            )
        }
    }
}

@Composable
private fun PreviewButton(
    onClick: () -> Unit
) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple,
            contentColor = Color.White
        )
    ) {

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Vista previa",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}