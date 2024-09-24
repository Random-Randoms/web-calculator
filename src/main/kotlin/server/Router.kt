@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.example.server

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.thymeleaf.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import org.example.server.login.Session
import org.example.server.login.validUser
import ru.shiroforbes.config.RouterConfig
import java.io.File

@OptIn(FormatStringsInDatetimeFormats::class)
fun Routing.routes(routerConfig: RouterConfig?) {
    staticFiles("/static", File("src/main/resources/static/"))

    get("/favicon.ico") {
        call.respondFile(File("src/main/resources/static/images/silly-cat-bleh.jpg"))
    }

    get("/") {
        call.respond(ThymeleafContent("login", mapOf()))
    }

    get("/login") {
        call.respond(
            ThymeleafContent(
                "login",
                mapOf(),
            ),
        )
    }

    post("/login") {
        val formContent = call.receiveText()
        val params = (Json.parseToJsonElement(formContent) as JsonObject).toMap()
        val name = params.jsonValue("login")
        val password = params.jsonValue("password")
        if (!validUser(name, password)) {
            return@post
        }
        call.sessions.set(Session(name, password))
        call.respondRedirect("/calculator")
    }

    authenticate("auth-session") {

        get("/calculator") {
            call.respond(
                ThymeleafContent(
                    "modernStyleCalculator",
                    mapOf(),
                )
            )
        }

        post("/calculator/evaluate") {
            val content = call.receiveText()
            val params = (Json.parseToJsonElement(content) as JsonObject).toMap()
            val who = call.sessions.get<Session>()?.login
            val expression = params.jsonValue("expression")
            val evaluated = 0
            call.respond(Json.encodeToJsonElement(evaluated))
        }
    }

}

private fun Map<String, JsonElement>.jsonValue(key: String): String {
    val quoted = this[key].toString()
    return quoted.substring(1, quoted.length - 1)
}