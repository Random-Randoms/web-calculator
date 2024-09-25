@file:Suppress("ktlint:standard:no-wildcard-imports")

package modelTests

import org.example.model.*
import kotlin.math.abs
import kotlin.test.Test

class ExpressionParserTest {
    private fun doubleResult(input: String): Double {
        val res = ExpressionParser(input).parseString()
        assert(res is RealResult)
        return (res as RealResult).value
    }

    private fun longResult(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun errorResult(input: String): ParsingError {
        val res = ExpressionParser(input).parseString()
        assert(res is ErrorResult)
        return (res as ErrorResult).error
    }

    private fun doubleEqual(
        left: Double,
        right: Double,
    ): Boolean {
        return abs(right - left) <= abs(right).coerceAtLeast(abs(left)) * 1e-9
    }

    private fun sumLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun sumRealToReal(input: String): Double {
        val res = ExpressionParser(input).parseString()
        assert(res is RealResult)
        return (res as RealResult).value
    }

    private fun sumRealToLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun multLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun multRealToReal(input: String): Double {
        val res = ExpressionParser(input).parseString()
        assert(res is RealResult)
        return (res as RealResult).value
    }

    private fun multRealToLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun divLongToLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun divLongToReal(input: String): Double {
        val res = ExpressionParser(input).parseString()
        assert(res is RealResult)
        return (res as RealResult).value
    }

    private fun divRealToLong(input: String): Long {
        val res = ExpressionParser(input).parseString()
        assert(res is IntegerResult)
        return (res as IntegerResult).value
    }

    private fun divRealToReal(input: String): Double {
        val res = ExpressionParser(input).parseString()
        assert(res is RealResult)
        return (res as RealResult).value
    }

    @Test
    fun doubleSingleton() {
        assert(doubleEqual(doubleResult("12.3"), 12.3))
        assert(doubleEqual(doubleResult("15.8"), 15.8))
        assert(doubleEqual(doubleResult("0.1"), 0.1))
    }

    @Test
    fun longSingleton() {
        assert(longResult("3") == 3.toLong())
        assert(longResult("0") == 0.toLong())
        assert(longResult("-1") == (-1).toLong())
    }

    @Test
    fun errorTest() {
        assert(errorResult("1%0").description == "Unknown operator")
        // assert(errorResult("2^3").description == "Unknown operator")
        assert(errorResult("2*").description == "Unknown error")
        assert(errorResult("1.2.3").description == "Incorrect number")
    }

    @Test
    fun sumTest() {
        assert(sumLong("1+2") == 3.toLong())
        assert(sumLong("5+0") == 5.toLong())
        assert(sumLong("1-2") == (-1).toLong())
        assert(sumLong("1+2-3") == (0).toLong())
        assert(doubleEqual(sumRealToReal("1.2+2.5"), 3.7))
        assert(doubleEqual(sumRealToReal("8.4+3.4"), 11.8))
        assert(doubleEqual(sumRealToReal("0.1+2"), 2.1))
        assert(sumRealToLong("0.2+0.8") == 1.toLong())
        assert(sumRealToLong("1.6-3.6") == (-2).toLong())
        assert(sumRealToLong("5.2-5.2") == 0.toLong())
    }

    @Test
    fun multTest() {
        assert(multLong("5*0") == 0.toLong())
        assert(multLong("7*2") == 14.toLong())
        assert(multLong("8*(-3)") == (-24).toLong())
        assert(doubleEqual(multRealToReal("1.2*9.5"), 11.4))
        assert(doubleEqual(multRealToReal("1.2*9.5"), 11.4))
        assert(doubleEqual(multRealToReal("2.5*(-1.5)"), -3.75))
        assert(multRealToLong("2.5*0.4") == 1.toLong())
        assert(multRealToLong("3.2*0.625") == 2.toLong())
        assert(multRealToLong("2.4*2.5") == 6.toLong())
    }

    @Test
    fun divTest() {
        assert(divLongToLong("4/2") == 2.toLong())
        assert(divLongToLong("0/3") == 0.toLong())
        assert(divLongToLong("12/(-4)") == (-3).toLong())
        assert(doubleEqual(divLongToReal("25/10"), 2.5))
        assert(doubleEqual(divLongToReal("12/(-5)"), -2.4))
        assert(doubleEqual(divLongToReal("2/5"), 0.4))
        assert(divRealToLong("2.5/(1/6)") == 15.toLong())
        assert(divRealToLong("3.2/(-0.1)") == (-32).toLong())
        assert(divRealToLong("0/0.52") == 0.toLong())
        assert(doubleEqual(divRealToReal("3.6/2.5"), 1.44))
        assert(doubleEqual(divRealToReal("-4.7/(-1.6)"), 2.9375))
        assert(doubleEqual(divRealToReal("12.5/1.6"), 7.8125))
    }

    @Test
    fun powTest() {
        assert(ExpressionParser("2^3").parseString().toString() == "8")
        assert(ExpressionParser("2^3^2").parseString().toString() == "512")
        assert(ExpressionParser("2*2^3").parseString().toString() == "16")
        assert(ExpressionParser("2^3*4^5").parseString().toString() == "8192")
        assert(ExpressionParser("2^3+3^2").parseString().toString() == "17")
        assert(ExpressionParser("2^(3-2)").parseString().toString() == "2")
        assert(ExpressionParser("(-1)^(0.5)").parseString().toString() == "NaN")
        assert(ExpressionParser("16^(1/2)").parseString().toString() == "4")
        assert(ExpressionParser("(2^(1/3))^3").parseString().toString() == "2")
    }

    @Test
    fun randomOperations() {
        var res = ExpressionParser("8/4/2").parseString()
        assert(res.toString() == "1")
        res = ExpressionParser("(5+3-1)*9").parseString()
        assert(res.toString() == "63")
        res = ExpressionParser("0/(0.2*0.678)").parseString()
        assert(res.toString() == "0")
        res = ExpressionParser("(10/2.5+6)*0.1").parseString()
        assert(res.toString() == "1")
        res = ExpressionParser("1*2*3*4").parseString()
        assert(res.toString() == "24")
    }

    @Test
    fun smoke() {
        val res = ExpressionParser("(1+2) / (3 / 2 / (0.25)) + 3 / (-2)").parseString()
        assert(res.toString() == "-1")
    }
}
