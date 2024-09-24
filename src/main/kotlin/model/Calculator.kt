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
        val operator = nextSymbol() ?: return left
        dropFirst()

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

        val first = nextSymbol() ?: return left

        if (first == '*') {
            dropFirst()
            return left * parseTerm()
        }

        if (first == '/') {
            dropFirst()
            return left / parseTerm()
        }

        return left
    }

    private fun parseMultiplier(): Double {
        val first = nextSymbol() ?: throw ParsingException(ParsingError.Unknown)

        if (isOpeningBracket(first)) {
            dropFirst()
            return parseExpression()
        }

        if (isDigit(first) || first == '-') {
            return parseNumber()
        }

        throw ParsingException(ParsingError.Unknown)
    }

    private fun parseNumber(): Double {
        try {
            val string = input.takeWhile { isDigit(it) || isDecimalPoint(it) || it == '-' }
            input = input.drop(string.count())
            return string.joinToString("").toDouble()
        } catch (e: NumberFormatException) {
            throw ParsingException(ParsingError.IncorrectNumber)
        } catch (e: Exception) {
            throw ParsingException(ParsingError.Unknown)
        }
    }

    private fun nextSymbol(): Char? {
        while (input.firstOrNull() == ' ') {
            dropFirst()
        }

        return input.firstOrNull()
    }

    private fun dropFirst() {
        input = input.drop(1)
    }
}
