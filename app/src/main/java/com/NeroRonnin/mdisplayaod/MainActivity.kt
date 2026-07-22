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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        MediaSessionHelper.syncCurrentSession(this)
    }

}