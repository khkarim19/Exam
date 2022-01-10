package ru.karim.ui.painting

import java.awt.*
import kotlin.math.roundToInt

class CartesianPainter(private val plane: CartesianPlane) : Painter {

//    public data class Pair<out A, out B>(
//        public val first: A, public val second: B
//    ) : Serializable {
//        public override fun toString(): String = "($first,$second)"
//    }

    fun round(x: Double): Double {
        var res: Double = x * 10
        res = res.roundToInt().toDouble()
        res /= 10
        return res
    }

    override fun paint(g: Graphics) {
        fun PaintAxes() {
            with(plane) {
                (g as Graphics2D).apply {
                    stroke = BasicStroke(2F)
                    if ((xMin > 0) or (xMax < 0)) {
                        drawLine(width, 0, width, height)
                        drawLine(0, 0, 0, height)
                    } else drawLine(xCrt2Scr(0.0), 0, xCrt2Scr(0.0), height)
                    if ((yMin > 0) or (yMax < 0)) {
                        drawLine(0, height, width, height)
                        drawLine(0, 0, width, 0)
                    } else drawLine(0, yCrt2Scr(0.0), width, yCrt2Scr(0.0))

                }
            }
        }

        fun PaintXs() {
            with(plane) {
                (g as Graphics2D).apply {
                    var scale = 0.3
                    var tSize = 4
                    var coeff = 10
                    if (Math.abs(xMin) + Math.abs(xMax) >= 20) coeff = 50
                    if (Math.abs(xMin) + Math.abs(xMax) <= 3) coeff = 1
                    stroke = BasicStroke(1F)
                    var n = round(xMin * 10)
                    font = Font("TimesNewRoman", Font.BOLD, 12)

                    while (n < xMax * 10) {
                        var (tW, tH) = fontMetrics.getStringBounds((n / 10).toString(), g)
                            .run { Pair(width.toFloat(), height.toFloat()) }
                        if (round(n) % coeff == 0.0) {
                            color = Color.RED
                            drawLine(xCrt2Scr(n / 10), yCrt2Scr(-scale), xCrt2Scr(n / 10), yCrt2Scr(scale))
                            color = Color.BLACK
                            drawString(round(n / 10).toString(), xCrt2Scr(n / 10) - tW / 2, yCrt2Scr(0.0) + tH + tSize)
                        } else if ((round(n) % 5 == 0.0) and (coeff != 1)) {
                            color = Color.BLUE
                            drawLine(xCrt2Scr(n / 10), yCrt2Scr(-scale / 2), xCrt2Scr(n / 10), yCrt2Scr(scale / 2))
                        } else if (coeff != 50) {
                            color = Color.BLACK
                            drawLine(xCrt2Scr(n / 10), yCrt2Scr(-scale / 3), xCrt2Scr(n / 10), yCrt2Scr(scale / 3))
                        }
                        n += 1
                    }
                }
            }
        }

        fun PaintYs() {
            with(plane) {
                (g as Graphics2D).apply {
                    var scale = 0.2
                    var tSize = 4
                    stroke = BasicStroke(1F)
                    var coeff = 10
                    if (Math.abs(yMin) + Math.abs(yMax) >= 20) coeff = 50
                    if (Math.abs(yMin) + Math.abs(yMax) <= 2) coeff = 1
                    var n = round(yMin * 10)
                    font = Font("TimesNewRoman", Font.BOLD, 12)

                    while (n < yMax * 10) {
                        var (tW, tH) = fontMetrics.getStringBounds((n / 10).toString(), g)
                            .run { Pair(width.toFloat(), height.toFloat()) }
                        if ((n % coeff == 0.0) and (n != 0.0)) {
                            color = Color.RED
                            drawLine(xCrt2Scr(-scale), yCrt2Scr(n / 10), xCrt2Scr(scale), yCrt2Scr(n / 10))
                            color = Color.BLACK
                            drawString(round(n / 10).toString(), xCrt2Scr(0.0) + tH + tSize, yCrt2Scr(n / 10) + tH / 4)

                        } else if ((round(n) % 5 == 0.0) and (coeff != 1)) {
                            color = Color.BLUE
                            drawLine(xCrt2Scr(-scale / 2), yCrt2Scr(n / 10), xCrt2Scr(scale / 2), yCrt2Scr(n / 10))
                        } else if (coeff != 50) {
                            color = Color.BLACK
                            drawLine(xCrt2Scr(-scale / 4), yCrt2Scr(n / 10), xCrt2Scr(scale / 4), yCrt2Scr(n / 10))
                        }
                        n += 1
                    }
                }
            }
        }

        PaintAxes()
        PaintXs()
        PaintYs()
    }
}

