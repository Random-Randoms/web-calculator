@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.example.server

import dbs.Equation
import dbs.addQuery
import dbs.forEachEquationOfUser
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
import org.example.model.ExpressionParser
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
        val session = Session(params.jsonValue("login"), params.jsonValue("password"))
        if (!validUser(session)) {
            return@post
        }
        call.sessions.set(session)
        call.respondRedirect("/calculator")
    }

    authenticate("auth-session") {

        get("/calculator") {
            val history = mutableListOf<Pair<String, String>>()
            forEachEquationOfUser(call.sessions.get<Session>()!!.id()!!) {
                history.add(this.expression to this.result)
            }
            call.respond(
                ThymeleafContent(
                    "modernStyleCalculator",
                    mapOf("history" to history),
                )
            )
        }

        post("/calculator/evaluate") {
            val content = call.receiveText()
            val params = (Json.parseToJsonElement(content) as JsonObject).toMap()
            val who = call.sessions.get<Session>()?.id()!!
            val expression = params.jsonValue("expression")
            val evaluated = ExpressionParser(expression).parseString().toString()
            addQuery(who, Equation(expression, evaluated))
            call.respond(Json.encodeToJsonElement(evaluated))
        }
    }

}

private fun Map<String, JsonElement>.jsonValue(key: String): String {
    val quoted = this[key].toString()
    return quoted.substring(1, quoted.length - 1)
}