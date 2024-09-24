package org.example.model

import kotlinx.serialization.Serializable

@Serializable
class ErrorResult(val error: ParsingError) : Result {
    override fun toString(): String = error.description
}
