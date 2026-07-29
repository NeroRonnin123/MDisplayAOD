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
     * Portada que realmente estamos mostrando.
     */
    var displayedArtwork by remember {
        mutableStateOf<Bitmap?>(song.albumArt)
    }

    /*
     * Canción a la que pertenece la portada
     * que actualmente estamos mostrando.
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
     * Controla la capa negra de transición.
     */
    var blackScreen by remember {
        mutableStateOf(false)
    }

    val blackAlpha by animateFloatAsState(
        targetValue = if (blackScreen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 180
        ),
        label = "ArtworkBlackFade"
    )

    /*
     * La transición sigue reaccionando cuando llega
     * un artwork, pero verificamos también a qué
     * canción pertenece.
     */
    LaunchedEffect(
        song.albumArt,
        song.title,
        song.artist
    ) {

        val newArtwork = song.albumArt

        val newSongKey =
            "${song.title}|${song.artist}"

        /*
         * Todavía no llegó portada.
         *
         * Conservamos la anterior.
         */
        if (newArtwork == null) {
            return@LaunchedEffect
        }

        /*
         * Primera portada.
         *
         * No hacemos transición porque no existe
         * una portada anterior.
         */
        if (displayedArtwork == null) {

            displayedArtwork = newArtwork
            displayedSongKey = newSongKey

            return@LaunchedEffect
        }

        /*
         * MUY IMPORTANTE:
         *
         * Spotify puede mandar varias instancias Bitmap
         * para exactamente la misma canción.
         *
         * Si seguimos en la misma canción,
         * NO volvemos a hacer fade.
         */
        if (displayedSongKey == newSongKey) {

            /*
             * Podemos quedarnos con el Bitmap más reciente
             * sin disparar ninguna transición.
             */
            displayedArtwork = newArtwork

            return@LaunchedEffect
        }

        /*
         * Llegamos aquí únicamente cuando:
         *
         * canción anterior != canción nueva
         *
         * Por lo tanto sí queremos transición.
         */

        // Portada anterior -> negro
        blackScreen = true

        // Esperamos a que termine el fade-out.
        delay(180)

        /*
         * Cambiamos la portada mientras
         * la pantalla está negra.
         */
        displayedArtwork = newArtwork
        displayedSongKey = newSongKey

        /*
         * Negro -> portada nueva.
         */
        blackScreen = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
         * Capa negra de transición.
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