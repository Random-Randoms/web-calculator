package org.example.model

import kotlinx.serialization.Serializable

@Serializable
class RealResult(val value: Double) : Result {
    override fun toString(): String = value.toString()
}
