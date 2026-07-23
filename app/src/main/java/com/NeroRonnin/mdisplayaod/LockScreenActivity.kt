package com.NeroRonnin.mdisplayaod

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.NeroRonnin.mdisplayaod.service.MediaSessionHelper
import com.NeroRonnin.mdisplayaod.ui.screens.LockScreen
import com.NeroRonnin.mdisplayaod.ui.theme.MDisplayAODTheme
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat

class LockScreenActivity : ComponentActivity() {

    private val unlockReceiver = object : BroadcastReceiver() {

        override fun onReceive(
            context: Context?,
            intent: Intent?
        ) {

            if (intent?.action == ACTION_CLOSE_LOCK_SCREEN) {

                Log.d(
                    "MDisplayAOD_LOCK_ACTIVITY",
                    "Usuario desbloqueó → cerrando LockScreenActivity"
                )

                finish()
            }
        }
    }

    companion object {
        const val ACTION_CLOSE_LOCK_SCREEN =
            "com.NeroRonnin.mdisplayaod.CLOSE_LOCK_SCREEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onCreate"
        )

        // Permitir que nuestra Activity aparezca encima del Keyguard.
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        // Pantalla completamente inmersiva.
        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        val insetsController =
            WindowCompat.getInsetsController(
                window,
                window.decorView
            )

        insetsController.hide(
            WindowInsetsCompat.Type.systemBars()
        )

        insetsController.systemBarsBehavior =
            WindowInsetsControllerCompat
                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        MediaSessionHelper.syncCurrentSession(this)

        enableEdgeToEdge()

        ContextCompat.registerReceiver(
            this,
            unlockReceiver,
            IntentFilter(ACTION_CLOSE_LOCK_SCREEN),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        setContent {
            MDisplayAODTheme {
                LockScreen()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onResume"
        )

        MediaSessionHelper.syncCurrentSession(this)
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onNewIntent"
        )

        MediaSessionHelper.syncCurrentSession(this)
    }

    override fun onPause() {
        super.onPause()

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onPause"
        )
    }

    override fun onStop() {
        super.onStop()

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onStop"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(unlockReceiver)

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onDestroy"
        )

        super.onDestroy()
    }
}