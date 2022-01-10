package ru.karim.ui.painting.math

class Newton(private val points: MutableMap<Double, Double>): Polynom(), (Double) -> Double {

    init{
        val p = Polynom()
        for (i in points.keys.indices){
            p += Multiplication(i)*Difference(i)
        }
        coeff = p.coeff
    }
    var fixOmega = Polynom(1.0)

    override operator fun invoke(x: Double): Double {
        return super.invoke(x)
    }

    override fun derivative(): Polynom {
        return super.derivative()
    }

    fun AddPoint(point: Pair<Double,Double>){
        points+=point
        val degree = points.size - 1
        this+=fixOmega*Difference(degree)
        fixOmega = fixOmega* Polynom(-point.first,1.0)
    }

    fun Multiplication(n: Int): Polynom {
        val multiplier = Polynom(1.0)
        points.keys.take(n).forEach{
            multiplier*= Polynom(-it, 1.0)
        }
        fixOmega = multiplier
        return multiplier
    }


    fun Difference(n: Int):Double {
        var summ = 0.0
        points.keys.take(n+1).forEach{xj ->
            var production = 1.0
            points.keys.take(n+1).forEach{ xi->
                if (xi != xj) production *= (xj-xi)
            }
            summ += points[xj]!!/production
        }
        return summ
    }

}