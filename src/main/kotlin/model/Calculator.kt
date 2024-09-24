package org.example.model

class ExpressionParser(string: String) {
    class ParsingException(val error: ParsingError) : Exception()

    companion object {
        val isDigit = { c: Char -> c in '0'..'9' }
        val isDecimalPoint = { c: Char -> c == '.' }
        val isOpeningBracket = { c: Char -> c == '(' }
    }

    private var input = string.asSequence()

    fun parseString(): Result {
        return try {
            resultOf(parseExpression())
        } catch (exception: ParsingException) {
            ErrorResult(exception.error)
        } catch (_: Exception) {
            ErrorResult(ParsingError.Unknown)
        }
    }

    private fun parseExpression(): Double {
        val left = parseTerm()
        val operator = input.firstOrNull() ?: return left

        input = input.drop(1)

        if (operator == '+') {
            return left + parseExpression()
        }

        if (operator == '-') {
            return left - parseExpression()
        }

        if (operator == ')') {
            return left
        }

        throw ParsingException(ParsingError.BadOperator)
    }

    private fun parseTerm(): Double {
        val left = parseMultiplier()

        val first = input.firstOrNull() ?: return left

        if (first == '*') {
            input = input.drop(1)
            return left * parseTerm()
        }

        if (first == '/') {
            input = input.drop(1)
            return left / parseTerm()
        }

        return left
    }

    private fun parseMultiplier(): Double {
        if (isOpeningBracket(input.first())) {
            input = input.drop(1)
            return parseExpression()
        }

        if (isDigit(input.first())) {
            return parseNumber()
        }

        throw ParsingException(ParsingError.Unknown)
    }

    private fun parseNumber(): Double {
        try {
            val string = input.takeWhile { isDigit(it) || isDecimalPoint(it) }
            input = input.drop(string.count())
            return string.joinToString("").toDouble()
        } catch (e: NumberFormatException) {
            throw ParsingException(ParsingError.IncorrectNumber)
        } catch (e: Exception) {
            throw ParsingException(ParsingError.Unknown)
        }
    }
}
