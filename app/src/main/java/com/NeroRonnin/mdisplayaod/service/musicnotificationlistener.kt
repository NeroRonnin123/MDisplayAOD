package com.NeroRonnin.mdisplayaod.service

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.os.Handler
import android.os.Looper
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.NeroRonnin.mdisplayaod.data.MusicRepository

class MusicNotificationListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MDisplayAOD_MUSIC"
        private const val SPOTIFY_PACKAGE = "com.spotify.music"

        // En la prueba real MediaSession llegó 27 ms
        // después de la notificación.
        private const val FALLBACK_GRACE_MS = 100L
    }

    private val handler =
        Handler(Looper.getMainLooper())

    private var pendingFallback: Runnable? = null


    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(
            TAG,
            "NotificationListener conectado"
        )

        MediaSessionHelper.syncCurrentSession(this)

        activeNotifications
            ?.firstOrNull {
                it.packageName == SPOTIFY_PACKAGE
            }
            ?.let { sbn ->
                procesarArtworkSpotify(sbn)
            }
    }


    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        cancelarFallbackPendiente()

        Log.d(
            TAG,
            "NotificationListener desconectado"
        )
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

        val extras =
            sbn.notification.extras

        val title =
            extras.getCharSequence(
                "android.title"
            )?.toString()

        val artist =
            extras.getCharSequence(
                "android.text"
            )?.toString()


        @Suppress("DEPRECATION")
        val artworkIcon =
            extras.getParcelable(
                "android.largeIcon"
            ) as? Icon
                ?: return


        val bitmap =
            try {

                (artworkIcon.loadDrawable(this) as? BitmapDrawable)
                    ?.bitmap

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "Error recuperando artwork Spotify",
                    e
                )

                null
            }
                ?: return


        programarFallback(
            bitmap = bitmap,
            title = title,
            artist = artist
        )
    }


    private fun programarFallback(
        bitmap: Bitmap,
        title: String?,
        artist: String?
    ) {

        /*
         * Si MediaSession ya tiene portada para esta canción,
         * ni siquiera programamos el fallback.
         */
        if (
            MusicRepository.hasMediaSessionArtwork(
                title = title,
                artist = artist
            )
        ) {

            Log.d(
                TAG,
                "Fallback ignorado: MediaSession ya tiene artwork | $title"
            )

            return
        }


        /*
         * Spotify publica muchas actualizaciones de la misma
         * notificación. Solo queremos un fallback pendiente.
         */
        cancelarFallbackPendiente()


        val runnable = Runnable {

            /*
             * Durante los 100 ms MediaSession pudo conseguir
             * la portada buena.
             */
            if (
                MusicRepository.hasMediaSessionArtwork(
                    title = title,
                    artist = artist
                )
            ) {

                Log.d(
                    TAG,
                    "Fallback cancelado: MediaSession ganó | $title"
                )

                pendingFallback = null
                return@Runnable
            }


            /*
             * updateAlbumArtFromNotification también verifica
             * que title/artist sigan correspondiendo a la
             * canción actual.
             */
            MusicRepository.updateAlbumArtFromNotification(
                albumArt = bitmap,
                title = title,
                artist = artist
            )


            Log.d(
                TAG,
                "Fallback ejecutado después de gracia | $title"
            )

            pendingFallback = null
        }


        pendingFallback = runnable

        handler.postDelayed(
            runnable,
            FALLBACK_GRACE_MS
        )
    }


    private fun cancelarFallbackPendiente() {

        pendingFallback?.let {
            handler.removeCallbacks(it)
        }

        pendingFallback = null
    }
}