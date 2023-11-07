package dev.appomart.sweetdelivery.domain.source.remote

import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus

interface IOrdersSource {

    suspend fun getOrders(): List<Order>

    suspend fun updateOrder(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    ): Boolean

}