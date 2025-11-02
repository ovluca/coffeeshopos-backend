package api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import domain.*

fun Route.registerV1Routes() {
    route("/v1") {
        route("/shops/{shopId}") {
            route("/menu") {
                get("/products") {
                    call.respond(sampleMenu())
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
                    call.respond(CreateOrderRes("ord_123", total))
                }
                get {
                    call.respond(listOf(OrderOut("ord_123", 1500, "PAID")))
                }
            }
        }
    }
}
