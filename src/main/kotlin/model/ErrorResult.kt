package org.example.model

import kotlinx.serialization.Serializable

@Serializable
class ErrorResult(val error: Error) : Result
