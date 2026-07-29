package com.NeroRonnin.mdisplayaod.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object ArtworkRemoteLoader {

    private const val TAG = "MDisplayAOD_REMOTE"

    /*
     * URL que actualmente estamos descargando.
     *
     * Spotify puede mandar muchos callbacks seguidos
     * con exactamente la misma URL.
     */
    private var loadingUrl: String? = null

    /*
     * Descarga una portada remota.
     *
     * Devuelve:
     *
     * Bitmap -> descarga correcta
     * null   -> error, URL inválida o descarga duplicada
     */
    suspend fun load(
        artworkUrl: String
    ): Bitmap? {

        /*
         * Evitamos descargar la misma URL
         * simultáneamente.
         */
        synchronized(this) {

            if (loadingUrl == artworkUrl) {

                Log.d(
                    TAG,
                    "REMOTE IGNORADO -> descarga ya en curso"
                )

                return null
            }

            loadingUrl = artworkUrl
        }

        return try {

            withContext(Dispatchers.IO) {

                Log.d(
                    TAG,
                    "REMOTE DESCARGANDO -> $artworkUrl"
                )

                val connection =
                    URL(artworkUrl)
                        .openConnection() as HttpURLConnection

                try {

                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000

                    connection.instanceFollowRedirects = true

                    connection.connect()

                    if (
                        connection.responseCode !in 200..299
                    ) {

                        Log.d(
                            TAG,
                            "REMOTE HTTP ERROR -> ${connection.responseCode}"
                        )

                        return@withContext null
                    }

                    connection.inputStream.use { inputStream ->

                        val bitmap =
                            BitmapFactory.decodeStream(
                                inputStream
                            )

                        if (bitmap != null) {

                            Log.d(
                                TAG,
                                "REMOTE OK -> " +
                                        "${bitmap.width}x${bitmap.height}"
                            )

                        } else {

                            Log.d(
                                TAG,
                                "REMOTE ERROR -> Bitmap null"
                            )
                        }

                        bitmap
                    }

                } finally {

                    connection.disconnect()
                }
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "REMOTE ERROR -> ${e.javaClass.simpleName}: ${e.message}",
                e
            )

            null

        } finally {

            synchronized(this) {

                if (loadingUrl == artworkUrl) {
                    loadingUrl = null
                }
            }
        }
    }
}