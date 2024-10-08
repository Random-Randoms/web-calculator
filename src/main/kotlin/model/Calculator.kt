package org.example.model

import kotlin.math.pow

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
        var result = parseTerm()

        while (true) {
            val operator = nextSymbol() ?: return result
            dropFirst()

            if (operator == '+') {
                result += parseTerm()
                continue
            }

            if (operator == '-') {
                result -= parseTerm()
                continue
            }

            if (operator == ')') {
                break
            }

            throw ParsingException(ParsingError.BadOperator)
        }

        return result
    }

    private fun parseTerm(): Double {
        var result = parseMultiplier()

        while (true) {
            val operator = nextSymbol() ?: return result

            if (operator == '*') {
                dropFirst()
                result *= parseMultiplier()
                continue
            }

            if (operator == '/') {
                dropFirst()
                result /= parseMultiplier()
                continue
            }

            break
        }

        return result
    }

    private fun parseMultiplier(): Double {
        val left = parsePower()
        val operator = nextSymbol() ?: return left

        if (operator == '^') {
            dropFirst()
            return left.pow(parseMultiplier())
        }

        return left
    }

    private fun parsePower(): Double {
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
            var sign = 1
            if (nextSymbol() == '-') {
                sign = -1
                dropFirst()
            }
            val string = input.takeWhile { isDigit(it) || isDecimalPoint(it) }
            input = input.drop(string.count())
            return string.joinToString("").toDouble() * sign
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
