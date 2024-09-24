package org.example.model

import kotlinx.serialization.Serializable

@Serializable
class IntegerResult(val value: Long) : Result {
    override fun toString(): String = value.toString()
}
