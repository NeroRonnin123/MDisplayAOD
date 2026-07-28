package com.NeroRonnin.mdisplayaod.service

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.NeroRonnin.mdisplayaod.data.MusicRepository

class MusicNotificationListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MDisplayAOD_MUSIC"
        private const val SPOTIFY_PACKAGE = "com.spotify.music"
    }

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(TAG, "NotificationListener conectado")

        MediaSessionHelper.syncCurrentSession(this)

        // Spotify puede estar reproduciendo antes de que
        // MDisplayAOD conecte el NotificationListener.
        activeNotifications
            ?.firstOrNull { it.packageName == SPOTIFY_PACKAGE }
            ?.let { sbn ->
                procesarArtworkSpotify(sbn)
            }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        Log.d(TAG, "NotificationListener desconectado")
    }

    override fun onNotificationPosted(
        sbn: StatusBarNotification?
    ) {
        super.onNotificationPosted(sbn)

        if (sbn?.packageName != SPOTIFY_PACKAGE) {
            return
        }

        procesarArtworkSpotify(sbn)
    }

    private fun procesarArtworkSpotify(
        sbn: StatusBarNotification
    ) {

        @Suppress("DEPRECATION")
        val artworkIcon =
            sbn.notification.extras
                .getParcelable("android.largeIcon") as? Icon
                ?: return

        try {

            val bitmap =
                (artworkIcon.loadDrawable(this) as? BitmapDrawable)
                    ?.bitmap
                    ?: return

            MusicRepository.updateAlbumArt(bitmap)

            Log.d(
                TAG,
                "Artwork Spotify recuperado desde notificación"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error recuperando artwork Spotify",
                e
            )
        }
    }
}