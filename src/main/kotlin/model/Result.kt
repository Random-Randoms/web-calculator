package org.example.model

import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.roundToLong

@Serializable
sealed interface Result {
    override fun toString(): String
}

fun resultOf(value: Double): Result {
    if (value in Long.MIN_VALUE.toDouble()..Long.MAX_VALUE.toDouble() &&
        abs(value - value.roundToLong().toDouble()) < 1e-12
    ) {
        return IntegerResult(value.roundToLong())
    }
    return RealResult(value)
}
