package com.NeroRonnin.mdisplayaod.service

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.util.Log
import com.NeroRonnin.mdisplayaod.data.ArtworkRemoteLoader
import com.NeroRonnin.mdisplayaod.data.MusicRepository
import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object MediaSessionHelper {

    private const val TAG = "MDisplayAOD_SESSION"
    private const val ARTWORK_TAG = "MDisplayAOD_ARTWORK"

    /*
     * Scope exclusivo para obtener portadas remotas.
     *
     * Dispatchers.IO evita hacer la descarga
     * en el hilo principal.
     */
    private val artworkScope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )

    private var activeController: MediaController? = null
    private var mediaSessionManager: MediaSessionManager? = null
    private var componentName: ComponentName? = null
    private var sessionsListenerRegistered = false

    /*
     * Spotify puede mandar muchos callbacks seguidos
     * con exactamente la misma URL.
     *
     * Guardamos la última solicitada para evitar
     * descargar la misma imagen 20 veces.
     */
    private var lastRemoteUrl: String? = null


    private val sessionsChangedListener =
        MediaSessionManager.OnActiveSessionsChangedListener {

            Log.d(
                TAG,
                "Cambió la lista de sesiones"
            )

            seleccionarSesion(
                it ?: emptyList()
            )
        }


    private val controllerCallback =
        object : MediaController.Callback() {

            override fun onMetadataChanged(
                metadata: MediaMetadata?
            ) {
                super.onMetadataChanged(metadata)

                Log.d(
                    TAG,
                    "Metadata cambió desde Helper"
                )

                activeController?.let { controller ->

                    actualizarCancion(
                        controller = controller,
                        metadata = metadata
                    )
                }
            }


            override fun onPlaybackStateChanged(
                state: PlaybackState?
            ) {
                super.onPlaybackStateChanged(state)

                Log.d(
                    TAG,
                    "Playback cambió desde Helper"
                )

                activeController?.let { controller ->

                    actualizarCancion(
                        controller = controller,
                        metadata = controller.metadata
                    )
                }
            }
        }


    fun syncCurrentSession(
        context: Context
    ) {

        val manager =
            context.getSystemService(
                Context.MEDIA_SESSION_SERVICE
            ) as MediaSessionManager

        val component =
            ComponentName(
                context,
                MusicNotificationListener::class.java
            )

        mediaSessionManager = manager
        componentName = component

        try {

            if (!sessionsListenerRegistered) {

                manager.addOnActiveSessionsChangedListener(
                    sessionsChangedListener,
                    component
                )

                sessionsListenerRegistered = true

                Log.d(
                    TAG,
                    "Listener de sesiones registrado"
                )
            }

            val controllers =
                manager.getActiveSessions(
                    component
                )

            Log.d(
                TAG,
                "Sincronizando sesiones: ${controllers.size}"
            )

            seleccionarSesion(
                controllers
            )

        } catch (e: SecurityException) {

            Log.e(
                TAG,
                "Sin acceso a MediaSession",
                e
            )
        }
    }


    private fun seleccionarSesion(
        controllers: List<MediaController>
    ) {

        val controller =
            controllers.firstOrNull {

                it.playbackState?.state ==
                        PlaybackState.STATE_PLAYING

            } ?: controllers.firstOrNull()


        /*
         * Ya no existe ninguna sesión.
         */
        if (controller == null) {

            activeController
                ?.unregisterCallback(
                    controllerCallback
                )

            activeController = null

            /*
             * Permitimos una nueva solicitud remota
             * cuando aparezca otra sesión.
             */
            lastRemoteUrl = null

            MusicRepository.updateSong(
                Song()
            )

            return
        }


        /*
         * Cambió la MediaSession activa.
         */
        if (
            activeController?.sessionToken !=
            controller.sessionToken
        ) {

            activeController
                ?.unregisterCallback(
                    controllerCallback
                )

            activeController =
                controller

            /*
             * Nueva sesión:
             * reiniciamos la deduplicación.
             */
            lastRemoteUrl = null

            controller.registerCallback(
                controllerCallback
            )

            Log.d(
                TAG,
                "Sesión activa: ${controller.packageName}"
            )
        }


        actualizarCancion(
            controller
        )
    }


    private fun actualizarCancion(
        controller: MediaController,
        metadata: MediaMetadata? = controller.metadata
    ) {

        /*
         * ==========================================
         * DATOS DE LA CANCIÓN
         * ==========================================
         */

        val title =
            metadata?.getString(
                MediaMetadata.METADATA_KEY_TITLE
            )

        val artist =
            metadata?.getString(
                MediaMetadata.METADATA_KEY_ARTIST
            )


        /*
         * ==========================================
         * ARTWORK NORMAL DE ANDROID
         * ==========================================
         */

        val newAlbumArt =
            metadata?.getBitmap(
                MediaMetadata.METADATA_KEY_ALBUM_ART
            )
                ?: metadata?.getBitmap(
                    MediaMetadata.METADATA_KEY_ART
                )


        /*
         * ==========================================
         * ARTWORK HTTPS DE SPOTIFY
         * ==========================================
         *
         * En el POCO descubrimos que Spotify expone
         * directamente esta URL aunque MediaSession
         * no entregue Bitmap.
         */

        val spotifyHttpsArtUri =
            metadata?.getString(
                "com.spotify.music.extra.ART_HTTPS_URI"
            )


        /*
         * ==========================================
         * PLAYBACK
         * ==========================================
         */

        val isPlaying =
            controller.playbackState?.state ==
                    PlaybackState.STATE_PLAYING


        Log.d(
            TAG,
            "Título: $title"
        )

        Log.d(
            TAG,
            "Artista: $artist"
        )

        Log.d(
            TAG,
            "Portada MediaSession: ${newAlbumArt != null}"
        )


        /*
         * ==========================================
         * FLUJO NORMAL DE MDISPLAYAOD
         * ==========================================
         *
         * Esto ocurre ANTES del fallback remoto.
         *
         * MusicRepository puede:
         *
         * - aceptar MediaSession
         * - buscar caché
         * - conservar Notification
         * - actualizar título/artista/playback
         */

        MusicRepository.updateSongFromMediaSession(
            song = Song(
                title = title ?: "Sin reproducción",
                artist = artist ?: "",
                albumArt = newAlbumArt,
                isPlaying = isPlaying
            ),
            mediaSessionHasArtwork =
                newAlbumArt != null
        )


        /*
         * ==========================================
         * FALLBACK HTTPS
         * ==========================================
         *
         * SOLO se utiliza cuando MediaSession
         * no entregó Bitmap.
         *
         * Notification NO se detiene.
         * Ambos mecanismos pueden trabajar
         * en paralelo.
         */

        if (
            newAlbumArt == null &&
            !spotifyHttpsArtUri.isNullOrBlank() &&
            !title.isNullOrBlank() &&
            !MusicRepository.hasArtworkForSong(
                title = title,
                artist = artist
            )
        ) {

            /*
             * Evitamos repetir la misma descarga.
             *
             * En nuestras pruebas Spotify mandó
             * la misma URL más de 20 veces en
             * unos pocos cientos de milisegundos.
             */
            if (
                lastRemoteUrl !=
                spotifyHttpsArtUri
            ) {

                lastRemoteUrl =
                    spotifyHttpsArtUri


                /*
                 * Capturamos los valores actuales.
                 *
                 * Es importante porque la coroutine
                 * terminará después y para entonces
                 * Spotify podría estar reproduciendo
                 * otra canción.
                 */
                val requestedUrl =
                    spotifyHttpsArtUri

                val requestedTitle =
                    title

                val requestedArtist =
                    artist ?: ""


                Log.d(
                    ARTWORK_TAG,
                    "REMOTE SOLICITADA -> " +
                            "$requestedTitle - " +
                            requestedArtist
                )


                artworkScope.launch {

                    /*
                     * ArtworkRemoteLoader realiza
                     * la descarga HTTPS.
                     */
                    val bitmap =
                        ArtworkRemoteLoader.load(
                            requestedUrl
                        )


                    if (bitmap != null) {

                        /*
                         * MusicRepository debe comprobar
                         * nuevamente que seguimos en
                         * requestedTitle/requestedArtist.
                         *
                         * De esta manera una descarga
                         * atrasada jamás debería poner
                         * la portada de la canción anterior.
                         */
                        MusicRepository
                            .updateAlbumArtFromRemote(
                                albumArt = bitmap,
                                title = requestedTitle,
                                artist = requestedArtist
                            )

                    } else {

                        /*
                         * Si falló la descarga, liberamos
                         * esta URL para que un callback
                         * posterior pueda reintentarlo.
                         */
                        if (
                            lastRemoteUrl ==
                            requestedUrl
                        ) {
                            lastRemoteUrl = null
                        }

                        Log.d(
                            ARTWORK_TAG,
                            "REMOTE FALLÓ -> " +
                                    requestedTitle
                        )
                    }
                }
            }
        }


        /*
         * ==========================================
         * CONTROLES
         * ==========================================
         */

        MusicRepository.setPlayPauseAction {

            if (
                controller.playbackState?.state ==
                PlaybackState.STATE_PLAYING
            ) {

                controller
                    .transportControls
                    .pause()

            } else {

                controller
                    .transportControls
                    .play()
            }
        }


        MusicRepository.setPreviousAction {

            controller
                .transportControls
                .skipToPrevious()
        }


        MusicRepository.setNextAction {

            controller
                .transportControls
                .skipToNext()
        }
    }
}