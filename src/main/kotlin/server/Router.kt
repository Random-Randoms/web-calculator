@file:Suppress("ktlint:standard:no-wildcard-imports")

package org.example.server

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
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

    get("/cat") {
        call.respondFile(File("src/main/resources/static/images/silly-cat-bleh.jpg"))
    }

    get("/lucky") {
        // IDK what is this for
        if (routerConfig == null)
            {
                call.respondFile(File("src/main/resources/static/images/silly-cat-bleh.jpg"))
            }
        call.respondRedirect(routerConfig!!.rickRollUrl)
    }
    TODO("more routes")
}
