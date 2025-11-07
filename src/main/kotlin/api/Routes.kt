package api

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import domain.*
import data.MongoMenuRepository
import java.util.UUID

fun Route.registerV1Routes() {
    val menuRepo = MongoMenuRepository()

    route("/v1") {
        route("/shops/{shopId}") {
            route("/menu") {
                get("/products") {
                    val shopId = call.parameters["shopId"]!!
                    val products = menuRepo.listProducts(UUID.fromString(shopId))
                    call.respond(products)
                }
                post("/products") {
                    val shopId = call.parameters["shopId"]!!
                    val req = call.receive<CreateProductReq>()
                    val product = menuRepo.createProduct(UUID.fromString(shopId), req.name, req.description)
                    call.respond(product)
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
