package com.NeroRonnin.mdisplayaod.service

import android.service.notification.NotificationListenerService
import android.util.Log

class MusicNotificationListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MDisplayAOD_MUSIC"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(TAG, "NotificationListener conectado")

        MediaSessionHelper.syncCurrentSession(this)
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        Log.d(TAG, "NotificationListener desconectado")
    }
}