package com.NeroRonnin.mdisplayaod.util

import android.graphics.Bitmap
import android.graphics.Color


object ArtworkColorExtractor {

    fun extractColor(bitmap: Bitmap): Int {

        // No necesitamos analizar la portada completa.
        // 48x48 = 2304 píxeles, suficiente para encontrar
        // los colores característicos.
        val scaledBitmap =
            Bitmap.createScaledBitmap(
                bitmap,
                48,
                48,
                true
            )

        // Agrupamos los colores por tono (Hue).
        // 36 grupos = bloques de 10 grados.
        val hueScores = FloatArray(36)
        val hueRed = LongArray(36)
        val hueGreen = LongArray(36)
        val hueBlue = LongArray(36)
        val hueCount = IntArray(36)

        val hsv = FloatArray(3)

        for (x in 0 until scaledBitmap.width) {
            for (y in 0 until scaledBitmap.height) {

                val pixel =
                    scaledBitmap.getPixel(x, y)

                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                Color.RGBToHSV(
                    red,
                    green,
                    blue,
                    hsv
                )

                val hue = hsv[0]
                val saturation = hsv[1]
                val brightness = hsv[2]

                // Quitamos:
                // - negros/sombras
                // - blancos
                // - grises
                // - colores extremadamente oscuros
                if (
                    saturation < 0.20f ||
                    brightness < 0.18f ||
                    brightness > 0.95f
                ) {
                    continue
                }

                val hueIndex =
                    ((hue / 10f).toInt())
                        .coerceIn(0, 35)

                /*
                 * Un píxel obtiene más peso si:
                 *
                 * 1. Tiene bastante saturación.
                 * 2. Tiene una luminosidad útil.
                 *
                 * Esto favorece colores visualmente
                 * interesantes sobre grises o tonos sucios.
                 */
                val idealBrightness =
                    1f - kotlin.math.abs(
                        brightness - 0.65f
                    )

                val score =
                    saturation *
                            saturation *
                            idealBrightness

                hueScores[hueIndex] += score

                hueRed[hueIndex] += red
                hueGreen[hueIndex] += green
                hueBlue[hueIndex] += blue

                hueCount[hueIndex]++
            }
        }

        val bestHue =
            hueScores.indices.maxByOrNull {
                hueScores[it]
            } ?: return Color.WHITE

        if (
            hueCount[bestHue] == 0 ||
            hueScores[bestHue] <= 0f
        ) {
            return Color.WHITE
        }

        var red =
            (hueRed[bestHue] / hueCount[bestHue])
                .toInt()

        var green =
            (hueGreen[bestHue] / hueCount[bestHue])
                .toInt()

        var blue =
            (hueBlue[bestHue] / hueCount[bestHue])
                .toInt()

        // Volvemos a HSV para hacer el color final
        // más apropiado para UI.
        Color.RGBToHSV(
            red,
            green,
            blue,
            hsv
        )

        // Evitamos colores demasiado apagados.
        hsv[1] =
            hsv[1]
                .coerceIn(
                    0.45f,
                    0.85f
                )

        // Y garantizamos suficiente luminosidad
        // para que el reloj sea legible.
        hsv[2] =
            hsv[2]
                .coerceIn(
                    0.70f,
                    0.95f
                )

        val finalColor =
            Color.HSVToColor(hsv)

        red = Color.red(finalColor)
        green = Color.green(finalColor)
        blue = Color.blue(finalColor)

        return Color.rgb(
            red,
            green,
            blue
        )
    }
}