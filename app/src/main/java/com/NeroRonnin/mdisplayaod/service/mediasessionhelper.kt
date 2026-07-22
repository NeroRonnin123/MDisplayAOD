package com.NeroRonnin.mdisplayaod.service

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.util.Log
import com.NeroRonnin.mdisplayaod.data.MusicRepository
import com.NeroRonnin.mdisplayaod.model.Song

object MediaSessionHelper {

    private const val TAG = "MDisplayAOD_SESSION"

    private var activeController: MediaController? = null

    private val controllerCallback = object : MediaController.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadata?) {
            super.onMetadataChanged(metadata)

            Log.d(TAG, "Metadata cambió desde Helper")

            activeController?.let { controller ->
                actualizarCancion(
                    controller = controller,
                    metadata = metadata
                )
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackState?) {
            super.onPlaybackStateChanged(state)

            Log.d(TAG, "Playback cambió desde Helper")

            activeController?.let { controller ->
                actualizarCancion(
                    controller = controller,
                    metadata = controller.metadata
                )
            }
        }
    }

    fun syncCurrentSession(context: Context) {

        val mediaSessionManager =
            context.getSystemService(Context.MEDIA_SESSION_SERVICE)
                    as MediaSessionManager

        val componentName = ComponentName(
            context,
            MusicNotificationListener::class.java
        )

        try {

            val controllers =
                mediaSessionManager.getActiveSessions(componentName)

            Log.d(TAG, "Sincronizando sesiones: ${controllers.size}")

            val controller =
                controllers.firstOrNull {
                    it.playbackState?.state == PlaybackState.STATE_PLAYING
                } ?: controllers.firstOrNull()

            if (controller == null) {

                activeController?.unregisterCallback(
                    controllerCallback
                )

                activeController = null

                MusicRepository.updateSong(Song())

                return
            }

            // Si cambió la sesión, quitamos el callback anterior
            if (activeController?.sessionToken != controller.sessionToken) {

                activeController?.unregisterCallback(
                    controllerCallback
                )

                activeController = controller

                activeController?.registerCallback(
                    controllerCallback
                )

                Log.d(TAG, "Callback registrado")
            }

            actualizarCancion(controller)

        } catch (e: SecurityException) {

            Log.e(
                TAG,
                "Sin acceso a MediaSession",
                e
            )
        }
    }

    private fun actualizarCancion(
        controller: MediaController,
        metadata: MediaMetadata? = controller.metadata
    ) {

        val title =
            metadata?.getString(
                MediaMetadata.METADATA_KEY_TITLE
            )

        val artist =
            metadata?.getString(
                MediaMetadata.METADATA_KEY_ARTIST
            )

        val albumArt =
            metadata?.getBitmap(
                MediaMetadata.METADATA_KEY_ALBUM_ART
            )
                ?: metadata?.getBitmap(
                    MediaMetadata.METADATA_KEY_ART
                )

        val isPlaying =
            controller.playbackState?.state ==
                    PlaybackState.STATE_PLAYING

        Log.d(TAG, "Título: $title")
        Log.d(TAG, "Artista: $artist")
        Log.d(TAG, "Portada: ${albumArt != null}")

        MusicRepository.updateSong(
            Song(
                title = title ?: "Sin reproducción",
                artist = artist ?: "",
                albumArt = albumArt,
                isPlaying = isPlaying
            )
        )

        MusicRepository.setPlayPauseAction {

            if (
                controller.playbackState?.state ==
                PlaybackState.STATE_PLAYING
            ) {
                controller.transportControls.pause()
            } else {
                controller.transportControls.play()
            }
        }

        MusicRepository.setPreviousAction {
            controller.transportControls.skipToPrevious()
        }

        MusicRepository.setNextAction {
            controller.transportControls.skipToNext()
        }
    }
}