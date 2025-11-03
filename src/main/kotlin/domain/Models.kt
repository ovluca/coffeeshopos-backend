package domain

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductReq(val name: String, val description: String? = null)

@Serializable
data class ProductOut(val id: String, val name: String, val description: String? = null)

@Serializable
data class CreateOrderReq(val items: List<OrderItemIn>)

@Serializable
data class OrderItemIn(val variantId: String, val qty: Int)

@Serializable
data class CreateOrderRes(val orderId: String, val totalCents: Int)

@Serializable
data class OrderOut(val id: String, val totalCents: Int, val status: String)
