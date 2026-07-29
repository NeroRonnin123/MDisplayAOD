package com.NeroRonnin.mdisplayaod.data

import android.graphics.Bitmap
import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object MusicRepository {

    enum class ArtworkSource {
        NONE,
        MEDIA_SESSION,
        NOTIFICATION
    }

    private val _song = MutableStateFlow(Song())

    val song: StateFlow<Song> = _song

    private var artworkSource: ArtworkSource =
        ArtworkSource.NONE

    private var artworkTitle: String? = null
    private var artworkArtist: String? = null

    private var onPlayPause: (() -> Unit)? = null
    private var onPrevious: (() -> Unit)? = null
    private var onNext: (() -> Unit)? = null


    fun updateSongFromMediaSession(
        song: Song,
        mediaSessionHasArtwork: Boolean
    ) {

        val currentSong = _song.value

        val songChanged =
            currentSong.title != song.title ||
                    currentSong.artist != song.artist

        if (songChanged) {

            artworkSource = ArtworkSource.NONE
            artworkTitle = null
            artworkArtist = null
        }

        /*
         * MediaSession SÍ trae artwork.
         */
        if (mediaSessionHasArtwork && song.albumArt != null) {

            /*
             * Si MediaSession ya tiene prioridad para esta misma canción,
             * no necesitamos volver a aceptar/publicar el artwork.
             *
             * IMPORTANTE:
             * seguimos actualizando isPlaying por si este callback
             * corresponde a play/pause.
             */
            if (
                artworkSource == ArtworkSource.MEDIA_SESSION &&
                artworkTitle == song.title &&
                artworkArtist == song.artist &&
                currentSong.albumArt != null
            ) {

                /*
                 * Solo publicamos un nuevo estado si realmente
                 * cambió playback.
                 */
                if (currentSong.isPlaying != song.isPlaying) {

                    _song.value = currentSong.copy(
                        isPlaying = song.isPlaying
                    )
                }

                return
            }

            /*
             * Primera portada válida de MediaSession
             * para esta canción.
             */
            android.util.Log.d(
                "MDisplayAOD_ARTWORK",
                "MEDIA_SESSION ACEPTADA -> ${song.title} | " +
                        "${song.albumArt.width}x${song.albumArt.height}"
            )

            artworkSource = ArtworkSource.MEDIA_SESSION
            artworkTitle = song.title
            artworkArtist = song.artist

            _song.value = song

            return
        }

        /*
         * MediaSession NO entregó portada.
         *
         * Si tenemos una portada proveniente de Notification
         * para ESTA canción, la conservamos.
         */
        val currentArtwork =
            if (
                artworkSource == ArtworkSource.NOTIFICATION &&
                artworkTitle == song.title &&
                artworkArtist == song.artist
            ) {
                currentSong.albumArt
            } else {
                null
            }

        val updatedSong =
            song.copy(
                albumArt = currentArtwork
            )

        /*
         * Evitamos publicar exactamente el mismo estado
         * una y otra vez.
         */
        if (
            currentSong.title == updatedSong.title &&
            currentSong.artist == updatedSong.artist &&
            currentSong.albumArt === updatedSong.albumArt &&
            currentSong.isPlaying == updatedSong.isPlaying
        ) {
            return
        }

        _song.value = updatedSong
    }


    fun updateAlbumArtFromNotification(
        albumArt: Bitmap,
        title: String?,
        artist: String?
    ) {

        if (
            _song.value.title != title ||
            _song.value.artist != artist
        ) {

            android.util.Log.d(
                "MDisplayAOD_ARTWORK",
                "NOTIFICATION RECHAZADA -> canción no coincide | " +
                        "repo=${_song.value.title} - ${_song.value.artist} | " +
                        "notif=$title - $artist"
            )

            return
        }

        if (
            artworkSource == ArtworkSource.MEDIA_SESSION &&
            artworkTitle == title &&
            artworkArtist == artist
        ) {

            android.util.Log.d(
                "MDisplayAOD_ARTWORK",
                "NOTIFICATION RECHAZADA -> MediaSession ya tiene prioridad | $title"
            )

            return
        }

        artworkSource = ArtworkSource.NOTIFICATION
        artworkTitle = title
        artworkArtist = artist

        _song.value =
            _song.value.copy(
                albumArt = albumArt
            )

        android.util.Log.d(
            "MDisplayAOD_ARTWORK",
            "NOTIFICATION ACEPTADA -> $title | ${albumArt.width}x${albumArt.height}"
        )
    }

    fun hasMediaSessionArtwork(
        title: String?,
        artist: String?
    ): Boolean {

        return artworkSource == ArtworkSource.MEDIA_SESSION &&
                artworkTitle == title &&
                artworkArtist == artist &&
                _song.value.albumArt != null
    }

    fun updateSong(song: Song) {

        if (song.albumArt == null) {
            artworkSource = ArtworkSource.NONE
            artworkTitle = null
            artworkArtist = null
        }

        _song.value = song
    }


    fun setPlayPauseAction(action: () -> Unit) {
        onPlayPause = action
    }


    fun playPause() {

        val currentSong = _song.value

        _song.value = currentSong.copy(
            isPlaying = !currentSong.isPlaying
        )

        onPlayPause?.invoke()
    }


    fun setPreviousAction(action: () -> Unit) {
        onPrevious = action
    }


    fun setNextAction(action: () -> Unit) {
        onNext = action
    }


    fun previous() {
        onPrevious?.invoke()
    }


    fun next() {
        onNext?.invoke()
    }
}