package org.example.server.login

import dbs.getUserIdOrNull
import io.ktor.server.auth.*
import org.jetbrains.exposed.dao.id.EntityID

data class Session(
    val login: String,
    val password: String,
) : Principal {
    fun id(): EntityID<Int>? {
        return getUserIdOrNull(login, password)
    }
}