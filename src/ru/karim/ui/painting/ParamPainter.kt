package ru.karim.ui.painting

import java.awt.*

class ParamPainter(private val plane: CartesianPlane) : Painter {

    var funColor: Color = Color.BLUE
    val t_min = -5.0
    val t_max = 5.0
    var functionX: (Double) -> Double = Math::sin
    var functionY: (Double) -> Double = Math::sin
    val steps = 3000

    override fun paint(g: Graphics) {
        with(g as Graphics2D) {
            color = funColor
            stroke = BasicStroke(2F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            val step = (t_max - t_min) / steps
            with(plane) {
                for (i in 0 until steps) {
                    val t = t_min + i * step
                    drawLine(
                        xCrt2Scr(functionX(t)),
                        yCrt2Scr(functionY(t)),
                        xCrt2Scr(functionX(t + step)),
                        yCrt2Scr(functionY(t + step)),
                    )
                }
            }
        }
    }
}