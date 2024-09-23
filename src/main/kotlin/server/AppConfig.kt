@file:Suppress("ktlint:standard:no-wildcard-imports")

package server

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import org.example.server.routes
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ru.shiroforbes.config.DbConfig
import ru.shiroforbes.config.RouterConfig

data class Config(
    val routerConfig: RouterConfig,
    val dbConfig: DbConfig,
)

fun Application.configureApp(config: Config?) {
    install(Thymeleaf) {
        setTemplateResolver(
            ClassLoaderTemplateResolver().apply {
                prefix = "templates/thymeleaf/"
                suffix = ".html"
                characterEncoding = "utf-8"
            },
        )
    }
    //  TODO("write authentication")
//    install(Sessions) {
//        cookie<Session>("user_session") {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 60 * 60 * 24 * 30
//        }
//    }
//
//    install(Authentication) {
//        form("auth-form") {
//            userParamName = "login"
//            passwordParamName = "password"
//            validate { credentials ->
//                UserHashedTableAuth(
//                    { it.toByteArray() },
//                    knownPasswords(),
//                ).authenticate(credentials)
//            }
//        }
//        session<Session>("auth-session") {
//            validate { session ->
//                if (validUser(session.login, session.password)) {
//                    session
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.respondRedirect("/login")
//            }
//        }
//
//        session<Session>("auth-session-no-redirect") {
//            skipWhen { call -> call.sessions.get<Session>() == null }
//            validate { session ->
//                if (validUser(session.login, session.password)) {
//                    session
//                } else {
//                    Session("", "")
//                }
//            }
//        }
//
//        session<Session>("auth-session-admin-only") {
//            validate { session ->
//                if (validAdmin(session.login, session.password)) {
//                    session
//                } else {
//                    null
//                }
//            }
//            challenge {
//                call.respondRedirect("/login")
//            }
//        }
//    }

    install(Routing) {
        routes(
            routerConfig = config?.routerConfig,
        )
    }
}
