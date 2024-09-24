package org.example.server.login

import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

// TODO("verify user")
fun validUser(
    login: String,
    password: String,
): Boolean {
    return runBlocking {
        return@runBlocking true
    }
}