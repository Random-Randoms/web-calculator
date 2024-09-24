@file:Suppress("ktlint:standard:no-wildcard-imports")

package server

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.thymeleaf.*
import org.example.server.login.Session
import org.example.server.login.validUser
import org.example.server.routes
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ru.shiroforbes.config.DbConfig
import ru.shiroforbes.config.RouterConfig

data class Config(
    val routerConfig: RouterConfig,
    val dbConfig: DbConfig,
)

fun Application.configureApp(config: Config?) {
    install(Thymeleaf) {
        installThymeleaf()
    }
    install(Sessions) {
        installSessions()
    }
    install(Authentication) {
        installAuthentication()
    }
    install(Routing) {
        routes(
            routerConfig = config?.routerConfig,
        )
    }
}

private fun AuthenticationConfig.installAuthentication() {
    session<Session>("auth-session") {
        authSession()
    }
    session<Session>("auth-session-no-redirect") {
        authSessionNoRedirect()
    }
}

private fun SessionAuthenticationProvider.Config<Session>.authSessionNoRedirect() {
    skipWhen { call -> call.sessions.get<Session>() == null }
    validate { session ->
        if (validUser(session.login, session.password)) {
            session
        } else {
            Session("", "")
        }
    }
}

private fun SessionAuthenticationProvider.Config<Session>.authSession() {
    validate { session ->
        if (validUser(session.login, session.password)) {
            session
        } else {
            null
        }
    }
    challenge {
        call.respondRedirect("/login")
    }
}

private fun SessionsConfig.installSessions() {
    cookie<Session>("user_session") {
        cookie.path = "/"
        cookie.maxAgeInSeconds = 60 * 60 * 24 * 30
    }
}

private fun TemplateEngine.installThymeleaf() {
    setTemplateResolver(
        ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        },
    )
}
