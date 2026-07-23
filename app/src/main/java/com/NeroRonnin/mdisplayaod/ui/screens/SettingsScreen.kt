package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp



@Composable
fun SettingsScreen(
    isEnabled: Boolean,
    overlayGranted: Boolean,
    notificationAccessGranted: Boolean,
    postNotificationsGranted: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onOverlayClick: () -> Unit,
    onNotificationAccessClick: () -> Unit,
    onPostNotificationsClick: () -> Unit,
    batteryOptimizationDisabled: Boolean,
    onBatteryOptimizationClick: () -> Unit,
    onPreviewClick: () -> Unit

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("MDisplayAOD")
        Text("Panel de administración")

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                if (isEnabled) {
                    "Pantalla de bloqueo activa"
                } else {
                    "Pantalla de bloqueo desactivada"
                }
            )

            Switch(
                checked = isEnabled,
                onCheckedChange = onEnabledChange
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Text("Permisos")

        PermissionRow(
            title = "Mostrar sobre otras apps",
            granted = overlayGranted,
            onClick = onOverlayClick
        )

        PermissionRow(
            title = "Acceso a notificaciones",
            granted = notificationAccessGranted,
            onClick = onNotificationAccessClick
        )

        PermissionRow(
            title = "Notificaciones de la aplicación",
            granted = postNotificationsGranted,
            onClick = onPostNotificationsClick
        )
        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(24.dp))

        Text("Funcionamiento en segundo plano")

        PermissionRow(
            title = "Optimización de batería de Android",
            granted = batteryOptimizationDisabled,
            onClick = onBatteryOptimizationClick
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onPreviewClick
        ) {
            Text("Vista previa")
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
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
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(title)

        Text(
            text = if (granted) {
                "✓ Concedido"
            } else {
                "⚠ Configurar"
            }
        )
    }
}