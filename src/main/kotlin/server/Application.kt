@file:Suppress("ktlint:standard:no-wildcard-imports")

package server

import dbs.DatabaseBuilder
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun init() {
    DatabaseBuilder.initialize()
}

fun main(args: Array<String>) {
    var port = 80
    for (arg in args) {
        if (arg.startsWith("--port=")) {
            port = arg.substringAfter("--port=").toInt()
        }
        if (arg == "--build") {
            DatabaseBuilder.build()
        }
        if (arg == "--destroy") {
            DatabaseBuilder.destroy()
        }
    }
    init()
    runBlocking {
        launch {
            embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
                .start(wait = true)
        }
    }
}

fun Application.module() {
    // TODO("Load config from .yaml file")
    configureApp(null)
}
