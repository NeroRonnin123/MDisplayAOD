package com.NeroRonnin.mdisplayaod.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.security.MessageDigest

object ArtworkCache {

    private const val TAG = "MDisplayAOD_CACHE"
    private const val CACHE_DIRECTORY = "artwork_cache"
    private const val PREFS_NAME = "artwork_cache_metadata"

    enum class Source {
        MEDIA_SESSION,
        NOTIFICATION
    }

    data class CachedArtwork(
        val bitmap: Bitmap,
        val source: Source,
        val width: Int,
        val height: Int
    )

    private var cacheDirectory: File? = null
    private var applicationContext: Context? = null


    fun initialize(context: Context) {

        if (cacheDirectory != null) {
            return
        }

        applicationContext =
            context.applicationContext

        val directory = File(
            context.applicationContext.filesDir,
            CACHE_DIRECTORY
        )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        cacheDirectory = directory

        Log.d(
            TAG,
            "ArtworkCache inicializado -> ${directory.absolutePath}"
        )
    }


    fun get(
        title: String,
        artist: String
    ): CachedArtwork? {

        val directory =
            cacheDirectory ?: return null

        val context =
            applicationContext ?: return null

        val key =
            buildKey(title, artist)

        val file =
            File(
                directory,
                "$key.webp"
            )

        if (!file.exists()) {

            Log.d(
                TAG,
                "CACHE MISS -> $title - $artist"
            )

            return null
        }

        return try {

            val bitmap =
                BitmapFactory.decodeFile(
                    file.absolutePath
                )

            if (bitmap == null) {

                Log.d(
                    TAG,
                    "CACHE INVALIDO -> $title - $artist"
                )

                return null
            }

            val preferences =
                context.getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )

            val sourceName =
                preferences.getString(
                    "${key}_source",
                    Source.NOTIFICATION.name
                )

            val source =
                try {
                    Source.valueOf(
                        sourceName
                            ?: Source.NOTIFICATION.name
                    )
                } catch (_: Exception) {
                    Source.NOTIFICATION
                }

            file.setLastModified(
                System.currentTimeMillis()
            )

            Log.d(
                TAG,
                "CACHE HIT -> $title - $artist | " +
                        "${bitmap.width}x${bitmap.height} | $source"
            )

            CachedArtwork(
                bitmap = bitmap,
                source = source,
                width = bitmap.width,
                height = bitmap.height
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error leyendo artwork del caché",
                e
            )

            null
        }
    }


    fun put(
        title: String,
        artist: String,
        bitmap: Bitmap,
        source: Source
    ) {

        val directory =
            cacheDirectory ?: return

        val context =
            applicationContext ?: return

        val key =
            buildKey(title, artist)

        val file =
            File(
                directory,
                "$key.webp"
            )

        /*
         * Antes de escribir revisamos si ya existe
         * una portada mejor.
         */
        val existing =
            get(
                title = title,
                artist = artist
            )

        if (
            existing != null &&
            !shouldReplace(
                existing = existing,
                newBitmap = bitmap,
                newSource = source
            )
        ) {

            Log.d(
                TAG,
                "CACHE CONSERVADO -> $title - $artist | " +
                        "${existing.width}x${existing.height} | " +
                        existing.source
            )

            return
        }

        try {

            file.outputStream().use { output ->

                bitmap.compress(
                    Bitmap.CompressFormat.WEBP,
                    90,
                    output
                )
            }

            context
                .getSharedPreferences(
                    PREFS_NAME,
                    Context.MODE_PRIVATE
                )
                .edit()
                .putString(
                    "${key}_source",
                    source.name
                )
                .apply()

            Log.d(
                TAG,
                "CACHE GUARDADO -> $title - $artist | " +
                        "${bitmap.width}x${bitmap.height} | $source"
            )

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error guardando artwork en caché",
                e
            )
        }
    }


    private fun shouldReplace(
        existing: CachedArtwork,
        newBitmap: Bitmap,
        newSource: Source
    ): Boolean {

        /*
         * REGLA 1
         *
         * MediaSession puede mejorar una portada
         * proveniente de Notification.
         */
        if (
            newSource == Source.MEDIA_SESSION &&
            existing.source == Source.NOTIFICATION
        ) {
            return true
        }

        /*
         * REGLA 2
         *
         * Notification nunca degrada una portada
         * que ya vino de MediaSession.
         */
        if (
            newSource == Source.NOTIFICATION &&
            existing.source == Source.MEDIA_SESSION
        ) {
            return false
        }

        /*
         * REGLA 3
         *
         * Si ambas vienen del mismo tipo de fuente,
         * solamente aceptamos una resolución mayor.
         */
        val existingPixels =
            existing.width.toLong() *
                    existing.height.toLong()

        val newPixels =
            newBitmap.width.toLong() *
                    newBitmap.height.toLong()

        return newPixels > existingPixels
    }


    private fun buildKey(
        title: String,
        artist: String
    ): String {

        val normalized =
            "${title.trim().lowercase()}|" +
                    artist.trim().lowercase()

        val digest =
            MessageDigest
                .getInstance("SHA-256")
                .digest(
                    normalized.toByteArray()
                )

        return digest.joinToString("") {
            "%02x".format(it)
        }
    }
}