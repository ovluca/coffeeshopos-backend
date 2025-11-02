package app

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import kotlinx.serialization.Serializable

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) { json() }
    install(CallLogging)
    install(CORS) { anyHost(); allowNonSimpleContentTypes = true }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(cause.message ?: "Internal Server Error")
            throw cause
        }
    }

    routing {
        swaggerUI(path = "docs") { forwardRoot = true }
        get("/health") { call.respondText("OK") }
        // sample endpoints for demonstration
        route("/v1") {
            route("/shops/{shopId}") {
                route("/menu") {
                    get("/products") {
                        call.respond(listOf<ProductOut>()) // placeholder
                    }
                    post("/products") {
                        val req = call.receive<CreateProductReq>()
                        call.respond(ProductOut("p_${req.name.lowercase()}", req.name, req.description))
                    }
                }
                route("/orders") {
                    post {
                        val req = call.receive<CreateOrderReq>()
                        val total = req.items.sumOf { it.qty * 1000 }
                        call.respond(CreateOrderRes(orderId = "ord_123", totalCents = total))
                    }
                    get {
                        call.respond(listOf(OrderOut("ord_123", 1500, "PAID")))
                    }
                }
            }
        }
    }
}

@Serializable data class CreateProductReq(val name: String, val description: String? = null)
@Serializable data class ProductOut(val id: String, val name: String, val description: String? = null)
@Serializable data class CreateOrderReq(val items: List<OrderItemIn>)
@Serializable data class OrderItemIn(val variantId: String, val qty: Int)
@Serializable data class CreateOrderRes(val orderId: String, val totalCents: Int)
@Serializable data class OrderOut(val id: String, val totalCents: Int, val status: String)
