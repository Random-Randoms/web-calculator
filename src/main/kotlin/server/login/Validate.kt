package org.example.server.login

import dbs.getUserIdOrNull
import kotlinx.coroutines.runBlocking

fun validUser(
    session: Session,
): Boolean {
    return runBlocking {
        return@runBlocking session.id() != null
    }
}