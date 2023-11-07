package dev.appomart.sweetdelivery.domain

data class Order(
    val id: Int,
    val name: String,
    val description: String,
    var status: OrderStatus,
    var price: Int?,
    var commentary: String?,
    val latitude: Double,
    val longitude: Double,
)