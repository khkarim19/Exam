package ru.karim.ui.painting

import ru.karim.ui.painting.math.Newton
import ru.karim.ui.painting.math.Polynom
import kotlin.math.max

class CartesianPlane(
    xMin: Double,
    xMax: Double,
    yMin: Double,
    yMax: Double
) {
    /**
     * Ширина плоскости в пикселях
     */
    private var xSize: Int = 1

    /**
     * Высота плоскости в пикселях
     */
    private var ySize: Int = 1

    /**
     * Номер последнего видимого пикселя по ширине
     */
    var width: Int
        get() = xSize - 1
        set(value) {
            xSize = max(1, value)
        }

    /**
     * Номер последнего видимого пикселя по высоте
     */
    var height: Int
        get() = ySize - 1
        set(value) {
            ySize = max(1, value)
        }

    /**
     * Левая граница отображаемого отрезка по оси абсцисс
     */
    var xMin: Double = 0.0
        private set

    /**
     * Правая граница отображаемого отрезка по оси абсцисс
     */
    var xMax: Double = 0.0
        private set

    /**
     * Нижняя граница отображаемого отрезка по оси ординат
     */
    var yMin: Double = 0.0
        private set

    /**
     * Верхняя граница отображаемого отрезка по оси ординат
     */
    var yMax: Double = 0.0
        private set

    /**
     * Свойство для задания границ отображаемого отрезка по оси абсцисс
     */
    var xSegment: Pair<Double, Double>
        get() = Pair(xMin, xMax)
        set(value) {
            val k = if (value.first == value.second) 0.1 else 0.0
            xMin = value.first - k
            xMax = value.second + k
            if (xMin > xMax) xMin = xMax.also { xMax = xMin }
        }

    /**
     * Свойство для задания границ отображаемого отрезка по оси ординат
     */
    var ySegment: Pair<Double, Double>
        get() = Pair(yMin, yMax)
        set(value) {
            val k = if (value.first == value.second) 0.1 else 0.0
            yMin = value.first - k
            yMax = value.second + k
            if (yMin > yMax) yMin = yMax.also { yMax = yMin }
        }

    /**
     * Плотность экранных точек по оси абсцисс
     */
    val xDen: Double
        get() = width / (xMax - xMin)

    /**
     * Плотность экранных точек по оси ординат
     */
    val yDen: Double
        get() = height / (yMax - yMin)

    init {
        xSegment = Pair(xMin, xMax)
        ySegment = Pair(yMin, yMax)
    }

    /**
     * Преобразование абсциссы из декартовой системы координат в экранную
     *
     * @param x абсцисса точки в декартовой системе координат
     * @return абсцисса точки в экранной системе координат
     */
    fun xCrt2Scr(x: Double): Int {
        var r = (xDen * (x - xMin)).toInt()
        if (r < -width) r = -width
        if (r > 2 * width) r = 2 * width
        return r
    }

    /**
     * Преобразование абсциссы из экранной системы координат в декартовую
     *
     * @param x абсцисса точки в экранной системе координат
     * @return абсцисса точки в декартовой системе координат
     */
    fun xScr2Crt(x: Int): Double {
        var _x = x
        if (_x < -width) _x = -width
        if (_x > 2 * width) _x = 2 * width
        return _x / xDen + xMin
    }

    /**
     * Преобразование ординаты из декартовой системы координат в экранную
     *
     * @param y ордината точки в декартовой системе координат
     * @return ордината точки в экранной системе координат
     */
    fun yCrt2Scr(y: Double): Int {
        var r = (yDen * (yMax - y)).toInt()
        if (r < -height) r = -height
        if (r > 2 * height) r = 2 * height
        return r
    }

    /**
     * Преобразование ординаты из экранной системы координат в декартовую
     *
     * @param y ордината точки в экранной системе координат
     * @return ордината точки в декартовой системе координат
     */
    fun yScr2Crt(y: Int): Double {
        var _y = y
        if (_y < -height) _y = -height
        if (_y > 2 * height) _y = 2 * height
        return yMax - _y / yDen
    }

    var polynom = Newton(mutableMapOf())



    fun addPoint(pair: Pair<Double, Double>) {
        polynom.AddPoint(pair)
    }

    fun derivate(polynom: Polynom){
        polynom.derivative()
    }
}