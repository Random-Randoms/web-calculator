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

    @Test
    fun doubleSingleton() {
        assert(doubleEqual(doubleResult("12.3"), 12.3))
        assert(doubleEqual(doubleResult("15.8"), 15.8))
        assert(doubleEqual(doubleResult("0.1"), 0.1))
    }
}
