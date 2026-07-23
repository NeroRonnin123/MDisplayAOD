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

class LockScreenActivity : ComponentActivity() {

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

        Log.d(
            "MDisplayAOD_LOCK_ACTIVITY",
            "LockScreenActivity onDestroy"
        )
    }
}