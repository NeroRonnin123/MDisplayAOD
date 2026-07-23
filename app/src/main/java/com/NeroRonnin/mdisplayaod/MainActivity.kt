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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {

    companion object {
        private const val PREFS_NAME = "mdisplayaod_settings"
        private const val KEY_ENABLED = "lock_screen_enabled"
    }

    private var isLockScreenEnabled by mutableStateOf(true)

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

        val preferences =
            getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
            )

        isLockScreenEnabled =
            preferences.getBoolean(
                KEY_ENABLED,
                true
            )

        checkOverlayPermission()

        if (isLockScreenEnabled) {
            checkNotificationPermission()
        }

        setContent {
            MDisplayAODTheme {

                SettingsScreen(
                    isEnabled = isLockScreenEnabled,

                    onEnabledChange = { enabled ->

                        updateLockScreenEnabled(enabled)
                    },

                    onPreviewClick = {
                        openLockScreenPreview()
                    }
                )
            }
        }
    }

    private fun updateLockScreenEnabled(enabled: Boolean) {

        isLockScreenEnabled = enabled

        getSharedPreferences(
            PREFS_NAME,
            MODE_PRIVATE
        )
            .edit()
            .putBoolean(
                KEY_ENABLED,
                enabled
            )
            .apply()

        if (enabled) {

            checkNotificationPermission()

        } else {

            val intent =
                Intent(
                    this,
                    LockScreenService::class.java
                )

            stopService(intent)
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