package com.NeroRonnin.mdisplayaod.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onPreviewClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "MDisplayAOD"
        )

        Text(
            text = "Panel de administración"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = if (isEnabled) {
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

        Button(
            onClick = onPreviewClick
        ) {
            Text("Vista previa")
        }
    }
}