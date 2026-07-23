package com.NeroRonnin.mdisplayaod

import android.content.ComponentName
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.NeroRonnin.mdisplayaod.service.MusicNotificationListener
import com.NeroRonnin.mdisplayaod.ui.screens.LockScreen
import com.NeroRonnin.mdisplayaod.ui.theme.MDisplayAODTheme
import com.NeroRonnin.mdisplayaod.service.MediaSessionHelper
import android.content.Intent
import androidx.core.content.ContextCompat
import com.NeroRonnin.mdisplayaod.service.LockScreenService
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts




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

               Log.d("MDisplayAOD_ACTIVITY", "MainActivity onCreate")
        checkNotificationPermission()

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        MediaSessionHelper.syncCurrentSession(this)

        enableEdgeToEdge()

        setContent {
            MDisplayAODTheme {
                LockScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d("MDisplayAOD_ACTIVITY", "MainActivity onResume")
        MediaSessionHelper.syncCurrentSession(this)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        Log.d(
            "MDisplayAOD_ACTIVITY",
            "MainActivity onNewIntent - FROM_SCREEN_OFF=${
                intent.getBooleanExtra("FROM_SCREEN_OFF", false)
            }"
        )
    }

    override fun onPause() {
        super.onPause()
        Log.d("MDisplayAOD_ACTIVITY", "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MDisplayAOD_ACTIVITY", "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MDisplayAOD_ACTIVITY", "MainActivity onDestroy")
    }
}