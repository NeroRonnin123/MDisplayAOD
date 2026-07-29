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

    /*
     * Límites del caché.
     *
     * Se conservarán como máximo:
     * - 200 portadas
     * - 100 MB
     *
     * Si se supera cualquiera de los dos límites,
     * se eliminan primero las portadas menos
     * recientemente utilizadas.
     */
    private const val MAX_CACHE_FILES = 200
    private const val MAX_CACHE_SIZE_BYTES =
        100L * 1024L * 1024L

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

        /*
         * También validamos los límites al iniciar.
         *
         * Esto protege el caché incluso si una versión
         * anterior de la app dejó más archivos.
         */
        enforceCacheLimits()
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

                /*
                 * Si el archivo está corrupto,
                 * lo eliminamos para no volver
                 * a intentar leerlo.
                 */
                file.delete()

                context
                    .getSharedPreferences(
                        PREFS_NAME,
                        Context.MODE_PRIVATE
                    )
                    .edit()
                    .remove("${key}_source")
                    .apply()

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

            /*
             * LRU:
             *
             * Cada vez que utilizamos una portada,
             * renovamos lastModified.
             *
             * De esta manera las canciones que
             * escuchamos frecuentemente permanecen
             * en caché.
             */
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

            /*
             * Al guardar/reemplazar también cuenta
             * como uso reciente.
             */
            file.setLastModified(
                System.currentTimeMillis()
            )

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

            /*
             * Después de escribir conocemos el tamaño
             * real del archivo WebP.
             */
            enforceCacheLimits()

        } catch (e: Exception) {

            Log.e(
                TAG,
                "Error guardando artwork en caché",
                e
            )
        }
    }


    private fun enforceCacheLimits() {

        val directory =
            cacheDirectory ?: return

        val context =
            applicationContext ?: return

        /*
         * Solamente administramos nuestros WebP.
         */
        val files =
            directory
                .listFiles { file ->
                    file.isFile &&
                            file.extension.equals(
                                "webp",
                                ignoreCase = true
                            )
                }
                ?.toMutableList()
                ?: return

        var totalFiles =
            files.size

        var totalBytes =
            files.sumOf { file ->
                file.length()
            }

        /*
         * Estamos dentro de ambos límites.
         */
        if (
            totalFiles <= MAX_CACHE_FILES &&
            totalBytes <= MAX_CACHE_SIZE_BYTES
        ) {
            return
        }

        Log.d(
            TAG,
            "CACHE LIMPIEZA INICIADA -> " +
                    "$totalFiles archivos | " +
                    "${bytesToMb(totalBytes)} MB"
        )

        /*
         * El archivo con lastModified más antiguo
         * será eliminado primero.
         */
        val oldestFirst =
            files.sortedBy { file ->
                file.lastModified()
            }

        val preferences =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val editor =
            preferences.edit()

        for (file in oldestFirst) {

            /*
             * En cuanto volvemos a estar dentro
             * de AMBOS límites terminamos.
             */
            if (
                totalFiles <= MAX_CACHE_FILES &&
                totalBytes <= MAX_CACHE_SIZE_BYTES
            ) {
                break
            }

            val fileSize =
                file.length()

            /*
             * El nombre del archivo es directamente
             * nuestro SHA-256.
             */
            val key =
                file.nameWithoutExtension

            if (file.delete()) {

                totalFiles--
                totalBytes -= fileSize

                /*
                 * Eliminamos también la metadata
                 * asociada al archivo.
                 */
                editor.remove(
                    "${key}_source"
                )

                Log.d(
                    TAG,
                    "CACHE ELIMINADO LRU -> " +
                            "${file.name} | " +
                            "${fileSize / 1024L} KB"
                )
            }
        }

        editor.apply()

        Log.d(
            TAG,
            "CACHE LIMPIEZA FINALIZADA -> " +
                    "$totalFiles archivos | " +
                    "${bytesToMb(totalBytes)} MB"
        )
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


    private fun bytesToMb(
        bytes: Long
    ): String {

        return String.format(
            "%.2f",
            bytes.toDouble() /
                    (1024.0 * 1024.0)
        )
    }
}