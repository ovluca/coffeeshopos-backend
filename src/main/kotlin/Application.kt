package app

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import api.registerV1Routes
import db.DatabaseFactory

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    // Initialize database connection
    DatabaseFactory.init(environment)

    install(ContentNegotiation) { json() }
    install(CallLogging)
    install(CORS) { anyHost(); allowNonSimpleContentTypes = true }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(cause.message ?: "Internal Server Error", status = io.ktor.http.HttpStatusCode.InternalServerError)
            throw cause
        }
    }
    routing {
        swaggerUI(path = "docs", swaggerFile = "openapi/documentation.yaml")
        get("/health") { call.respondText("OK") }
        registerV1Routes()
    }
}
