package org.example.model

import kotlinx.serialization.Serializable

@Serializable
enum class Error(val description: String) {
    DivisionByZero("Division by zero"),
    BadBrackets("Incorrect bracket positioning"),
    BadOperand("Wrong amount of operands"),
    FractionalPowerOfNegative("Attempt to raise negative number to the fractional power"),
    ZeroToZero("Attempt to raise zero to the power of zero"),
    EmptyInput("Empty input"),
}
