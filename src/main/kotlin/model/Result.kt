package org.example.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Result

fun resultOf(value: Double): Result {
    return RealResult(value)
}
