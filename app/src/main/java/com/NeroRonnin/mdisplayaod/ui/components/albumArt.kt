package com.NeroRonnin.mdisplayaod.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.NeroRonnin.mdisplayaod.model.Song
import kotlinx.coroutines.delay

@Composable
fun AlbumArt(song: Song) {

    /*
     * Bitmap que realmente está dibujado en pantalla.
     *
     * No dependemos directamente de song.albumArt para
     * renderizar porque queremos controlar nosotros
     * cuándo ocurre el cambio visual.
     */
    var displayedArtwork by remember {
        mutableStateOf<Bitmap?>(song.albumArt)
    }

    /*
     * Canción a la que pertenece displayedArtwork.
     */
    var displayedSongKey by remember {
        mutableStateOf(
            if (song.albumArt != null) {
                "${song.title}|${song.artist}"
            } else {
                null
            }
        )
    }

    /*
     * Capa negra utilizada únicamente durante
     * el cambio REAL de canción.
     */
    var blackScreen by remember {
        mutableStateOf(false)
    }

    val blackAlpha by animateFloatAsState(
        targetValue =
            if (blackScreen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 180
        ),
        label = "ArtworkBlackFade"
    )

    LaunchedEffect(
        song.albumArt,
        song.title,
        song.artist
    ) {

        val newArtwork =
            song.albumArt

        val newSongKey =
            "${song.title}|${song.artist}"

        /*
         * Todavía no tenemos portada para la canción nueva.
         *
         * NO borramos la portada anterior.
         *
         * Esto evita mostrar negro mientras esperamos:
         *
         * CACHE
         * MediaSession
         * Notification
         * REMOTE
         */
        if (newArtwork == null) {
            return@LaunchedEffect
        }

        /*
         * Primera portada desde que se creó el composable.
         *
         * No necesitamos transición porque no existe
         * una portada anterior que ocultar.
         */
        if (displayedArtwork == null) {

            displayedArtwork =
                newArtwork

            displayedSongKey =
                newSongKey

            return@LaunchedEffect
        }

        /*
         * MISMA CANCIÓN.
         *
         * Ejemplo:
         *
         * Notification -> 72x72
         * REMOTE       -> 640x640
         *
         * Actualizamos el Bitmap inmediatamente,
         * pero NO hacemos otro fade.
         */
        if (
            displayedSongKey ==
            newSongKey
        ) {

            displayedArtwork =
                newArtwork

            return@LaunchedEffect
        }

        /*
         * CAMBIO REAL DE CANCIÓN.
         *
         * En este punto:
         *
         * displayedArtwork = portada anterior
         * newArtwork       = portada nueva
         */

        // Portada anterior -> negro.
        blackScreen = true

        /*
         * Dejamos terminar el fade-out.
         */
        delay(180)

        /*
         * Sustituimos el Bitmap mientras
         * la capa negra está encima.
         */
        displayedArtwork =
            newArtwork

        displayedSongKey =
            newSongKey

        /*
         * Negro -> portada nueva.
         */
        blackScreen = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        displayedArtwork?.let { artwork ->

            Image(
                bitmap = artwork.asImageBitmap(),
                contentDescription = "Portada",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(18.dp),
                contentScale = ContentScale.Crop
            )
        }

        /*
         * Capa utilizada únicamente
         * durante la transición.
         */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(
                        alpha = blackAlpha
                    )
                )
        )
    }
}