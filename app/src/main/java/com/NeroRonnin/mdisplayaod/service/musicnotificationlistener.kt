package com.NeroRonnin.mdisplayaod.service

import android.content.ComponentName
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.util.Log
import com.NeroRonnin.mdisplayaod.data.MusicRepository
import com.NeroRonnin.mdisplayaod.model.Song

class MusicNotificationListener : NotificationListenerService() {

    companion object {
        private const val TAG = "MDisplayAOD_MUSIC"
    }

    private lateinit var mediaSessionManager: MediaSessionManager
    private var activeController: MediaController? = null

    private val mediaControllerCallback = object : MediaController.Callback() {

        override fun onMetadataChanged(metadata: android.media.MediaMetadata?) {
            super.onMetadataChanged(metadata)

            Log.d(TAG, "Metadata cambió")

            activeController?.let {
                actualizarCancion(it)
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackState?) {
            super.onPlaybackStateChanged(state)

            Log.d(TAG, "Estado de reproducción cambió")

            activeController?.let {
                actualizarCancion(it)
            }
        }
    }

    private val sessionsChangedListener =
        MediaSessionManager.OnActiveSessionsChangedListener { controllers ->

            Log.d(TAG, "Cambio en sesiones multimedia")

            actualizarSesiones(controllers ?: emptyList())
        }

    override fun onListenerConnected() {
        super.onListenerConnected()

        Log.d(TAG, "NotificationListener conectado")

        mediaSessionManager =
            getSystemService(MEDIA_SESSION_SERVICE) as MediaSessionManager

        val componentName = ComponentName(
            this,
            MusicNotificationListener::class.java
        )

        try {

            mediaSessionManager.addOnActiveSessionsChangedListener(
                sessionsChangedListener,
                componentName
            )

            val controllers =
                mediaSessionManager.getActiveSessions(componentName)

            Log.d(TAG, "Sesiones iniciales: ${controllers.size}")

            actualizarSesiones(controllers)

        } catch (e: SecurityException) {

            Log.e(TAG, "Sin permiso para MediaSession", e)
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()

        Log.d(TAG, "NotificationListener desconectado")

        if (::mediaSessionManager.isInitialized) {
            mediaSessionManager.removeOnActiveSessionsChangedListener(
                sessionsChangedListener
            )
        }

        activeController?.unregisterCallback(
            mediaControllerCallback
        )

        activeController = null

    }
    private fun actualizarCancion(controller: MediaController) {

        val metadata = controller.metadata

        val title = metadata?.getString(
            MediaMetadata.METADATA_KEY_TITLE
        )

        val artist = metadata?.getString(
            MediaMetadata.METADATA_KEY_ARTIST
        )

        val albumArt =
            metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
                ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)

        // LOGS PARA SABER CÓMO SPOTIFY ENTREGA LA PORTADA
        Log.d(TAG, "Título: $title")
        Log.d(TAG, "Artista: $artist")
        Log.d(TAG, "AlbumArt Bitmap: ${albumArt != null}")

        Log.d(
            TAG,
            "AlbumArt URI: ${
                metadata?.getString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI)
            }"
        )

        Log.d(
            TAG,
            "Art URI: ${
                metadata?.getString(MediaMetadata.METADATA_KEY_ART_URI)
            }"
        )

        MusicRepository.updateSong(
            Song(
                title = title ?: "Sin reproducción",
                artist = artist ?: "",
                albumArt = albumArt,
                isPlaying =
                    controller.playbackState?.state ==
                            PlaybackState.STATE_PLAYING
            )
        )
    }

    private fun actualizarSesiones(
        controllers: List<MediaController>
    ) {

        Log.d(TAG, "Sesiones encontradas: ${controllers.size}")

        // Dejamos de escuchar la sesión anterior
        activeController?.unregisterCallback(
            mediaControllerCallback
        )

        activeController = controllers.firstOrNull()

        if (activeController == null) {

            MusicRepository.updateSong(
                Song()
            )

            return
        }

        activeController?.registerCallback(
            mediaControllerCallback
        )

        activeController?.let {
            actualizarCancion(it)
        }
    }
}