package com.NeroRonnin.mdisplayaod

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.NeroRonnin.mdisplayaod.service.LockScreenService
import com.NeroRonnin.mdisplayaod.ui.screens.SettingsScreen
import com.NeroRonnin.mdisplayaod.ui.theme.MDisplayAODTheme

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {
                startLockScreenService()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkOverlayPermission()
        checkNotificationPermission()

        setContent {
            MDisplayAODTheme {

                SettingsScreen(
                    onPreviewClick = {
                        openLockScreenPreview()
                    }
                )
            }
        }
    }

    private fun checkOverlayPermission() {

        if (!Settings.canDrawOverlays(this)) {

            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )

            startActivity(intent)
        }
    }

    private fun checkNotificationPermission() {

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {

            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )

        } else {

            startLockScreenService()
        }
    }

    private fun startLockScreenService() {

        val intent =
            Intent(this, LockScreenService::class.java)

        ContextCompat.startForegroundService(
            this,
            intent
        )
    }

    private fun openLockScreenPreview() {

        val intent =
            Intent(
                this,
                LockScreenActivity::class.java
            )

        startActivity(intent)
    }
}