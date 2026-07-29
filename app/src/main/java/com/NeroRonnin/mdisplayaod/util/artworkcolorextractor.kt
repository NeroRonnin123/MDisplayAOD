package com.NeroRonnin.mdisplayaod.util

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.abs
import kotlin.math.pow

data class ArtworkPalette(
    val primary: Int,
    val title: Int,
    val controls: Int
)

object ArtworkColorExtractor {

    // =========================================================
    // COLOR BASE
    // =========================================================

    fun extractColor(bitmap: Bitmap): Int {

        /*
         * Analizamos una versión pequeña.
         *
         * 48 x 48 = 2304 píxeles.
         * Es suficiente para detectar el color característico
         * sin procesar toda la portada.
         */
        val scaledBitmap =
            Bitmap.createScaledBitmap(
                bitmap,
                48,
                48,
                true
            )

        val hueScores = FloatArray(36)

        val hueRed = LongArray(36)
        val hueGreen = LongArray(36)
        val hueBlue = LongArray(36)

        val hueCount = IntArray(36)

        val hsv = FloatArray(3)

        var analyzedPixels = 0
        var chromaticPixels = 0

        for (x in 0 until scaledBitmap.width) {

            for (y in 0 until scaledBitmap.height) {

                val pixel =
                    scaledBitmap.getPixel(x, y)

                val red =
                    Color.red(pixel)

                val green =
                    Color.green(pixel)

                val blue =
                    Color.blue(pixel)

                Color.RGBToHSV(
                    red,
                    green,
                    blue,
                    hsv
                )

                val hue =
                    hsv[0]

                val saturation =
                    hsv[1]

                val brightness =
                    hsv[2]

                analyzedPixels++

                if (
                    saturation < 0.20f ||
                    brightness < 0.18f ||
                    brightness > 0.95f
                ) {
                    continue
                }

                chromaticPixels++

                /*
                 * Ignoramos:
                 *
                 * - colores casi grises
                 * - negros
                 * - sombras profundas
                 * - blancos casi puros
                 */
                if (
                    saturation < 0.20f ||
                    brightness < 0.18f ||
                    brightness > 0.95f
                ) {
                    continue
                }

                val hueIndex =
                    ((hue / 10f).toInt())
                        .coerceIn(
                            0,
                            35
                        )

                /*
                 * Favorecemos:
                 *
                 * - colores saturados
                 * - luminosidad intermedia
                 *
                 * Esto ayuda a encontrar colores
                 * visualmente representativos.
                 */
                val idealBrightness =
                    1f - abs(
                        brightness - 0.65f
                    )

                val score =
                    saturation *
                            saturation *
                            idealBrightness

                hueScores[hueIndex] +=
                    score

                hueRed[hueIndex] +=
                    red

                hueGreen[hueIndex] +=
                    green

                hueBlue[hueIndex] +=
                    blue

                hueCount[hueIndex]++
            }
        }

        // =====================================================
        // HUE DOMINANTE
        // =====================================================


        /*
 * Si prácticamente toda la portada es monocromática,
 * no dejamos que unos pocos píxeles con tinte
 * determinen el color de toda la interfaz.
 */
        val chromaticRatio =
            if (analyzedPixels > 0) {
                chromaticPixels.toFloat() /
                        analyzedPixels.toFloat()
            } else {
                0f
            }

        if (chromaticRatio < 0.08f) {
            return Color.WHITE
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

        val red =
            (
                    hueRed[bestHue] /
                            hueCount[bestHue]
                    ).toInt()

        val green =
            (
                    hueGreen[bestHue] /
                            hueCount[bestHue]
                    ).toInt()

        val blue =
            (
                    hueBlue[bestHue] /
                            hueCount[bestHue]
                    ).toInt()

        Color.RGBToHSV(
            red,
            green,
            blue,
            hsv
        )

        /*
         * Normalización inicial.
         *
         * Aquí todavía no nos preocupamos por
         * luminancia perceptual.
         *
         * Eso se hará al construir la paleta.
         */
        hsv[1] =
            hsv[1]
                .coerceIn(
                    0.40f,
                    0.78f
                )

        hsv[2] =
            hsv[2]
                .coerceIn(
                    0.65f,
                    0.95f
                )

        return Color.HSVToColor(
            hsv
        )
    }


    // =========================================================
    // PALETA
    // =========================================================

    fun extractPalette(
        bitmap: Bitmap
    ): ArtworkPalette {

        val baseColor =
            extractColor(bitmap)


        if (baseColor == Color.WHITE) {

            return ArtworkPalette(
                primary = Color.WHITE,
                title = Color.WHITE,
                controls = Color.WHITE
            )
        }

        val hsv =
            FloatArray(3)

        Color.colorToHSV(
            baseColor,
            hsv
        )


        // =====================================================
        // RELOJ
        // =====================================================

        val primaryHsv =
            hsv.copyOf()

        primaryHsv[1] =
            primaryHsv[1]
                .coerceIn(
                    0.30f,
                    0.72f
                )

        /*
         * Dejamos suficiente margen para que
         * ensureReadableColor pueda aclararlo.
         */
        primaryHsv[2] =
            primaryHsv[2]
                .coerceIn(
                    0.65f,
                    0.95f
                )

        val rawPrimary =
            Color.HSVToColor(
                primaryHsv
            )

        val primary =
            ensureReadableColor(
                color = rawPrimary,
                minimumLuminance = 0.30
            )


        // =====================================================
        // TÍTULO
        // =====================================================

        val titleHsv =
            hsv.copyOf()

        /*
         * El título será más suave:
         *
         * - menos saturación
         * - más luminosidad
         */
        titleHsv[1] =
            (titleHsv[1] * 0.60f)
                .coerceIn(
                    0.20f,
                    0.58f
                )

        titleHsv[2] =
            (titleHsv[2] + 0.18f)
                .coerceIn(
                    0.75f,
                    1f
                )

        val rawTitle =
            Color.HSVToColor(
                titleHsv
            )

        val title =
            ensureReadableColor(
                color = rawTitle,
                minimumLuminance = 0.42
            )


        // =====================================================
        // CONTROLES
        // =====================================================

        val controlsHsv =
            hsv.copyOf()

        /*
         * IMPORTANTE:
         *
         * Ya NO desplazamos el Hue.
         *
         * Los controles conservan exactamente
         * la familia cromática de la portada.
         */
        controlsHsv[1] =
            (controlsHsv[1] * 0.90f)
                .coerceIn(
                    0.30f,
                    0.68f
                )

        controlsHsv[2] =
            (controlsHsv[2] + 0.10f)
                .coerceIn(
                    0.72f,
                    1f
                )

        val rawControls =
            Color.HSVToColor(
                controlsHsv
            )

        val controls =
            ensureReadableColor(
                color = rawControls,
                minimumLuminance = 0.36
            )


        // =====================================================
        // RESULTADO
        // =====================================================

        return ArtworkPalette(
            primary = primary,
            title = title,
            controls = controls
        )
    }


    // =========================================================
    // GARANTIZAR LEGIBILIDAD
    // =========================================================

    private fun ensureReadableColor(
        color: Int,
        minimumLuminance: Double
    ): Int {

        /*
         * Si el color ya tiene suficiente
         * luminancia perceptual, no hacemos nada.
         */
        if (
            calculateLuminance(color) >=
            minimumLuminance
        ) {
            return color
        }

        val hsv =
            FloatArray(3)

        Color.colorToHSV(
            color,
            hsv
        )

        /*
         * Vamos aclarando progresivamente.
         *
         * Conservamos el Hue y reducimos ligeramente
         * la saturación conforme aclaramos.
         *
         * Esto evita convertir azules oscuros
         * en "azul eléctrico".
         */
        repeat(12) {

            hsv[2] =
                (hsv[2] + 0.04f)
                    .coerceAtMost(1f)

            hsv[1] =
                (hsv[1] * 0.96f)
                    .coerceAtLeast(0.15f)

            val candidate =
                Color.HSVToColor(
                    hsv
                )

            if (
                calculateLuminance(candidate) >=
                minimumLuminance
            ) {
                return candidate
            }
        }

        /*
         * Si llegamos aquí significa que el Hue
         * es especialmente oscuro perceptualmente.
         *
         * Devolvemos la versión más clara que
         * conseguimos en vez de abandonar el
         * color automático.
         */
        return Color.HSVToColor(
            hsv
        )
    }


    // =========================================================
    // LUMINANCIA PERCEPTUAL
    // =========================================================

    private fun calculateLuminance(
        color: Int
    ): Double {

        /*
         * Convertimos RGB sRGB a valores lineales.
         *
         * Esto se acerca mucho más a cómo nuestros
         * ojos perciben la luminosidad que simplemente
         * mirar HSV.value.
         */

        fun linearize(
            channel: Int
        ): Double {

            val value =
                channel / 255.0

            return if (
                value <= 0.04045
            ) {

                value / 12.92

            } else {

                (
                        (value + 0.055) /
                                1.055
                        ).pow(2.4)
            }
        }

        val red =
            linearize(
                Color.red(color)
            )

        val green =
            linearize(
                Color.green(color)
            )

        val blue =
            linearize(
                Color.blue(color)
            )

        /*
         * Luminancia relativa.
         *
         * El ojo humano percibe mucho más el verde,
         * después el rojo y bastante menos el azul.
         *
         * Por eso un azul "brillante" en HSV puede
         * seguir pareciendo oscuro.
         */
        return (
                0.2126 * red +
                        0.7152 * green +
                        0.0722 * blue
                )
    }
}