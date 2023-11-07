package dev.appomart.sweetdelivery.domain.repository

import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus

interface IOrderRepository {

    suspend fun getOrders(): List<Order>

    suspend fun updateOrderStatus(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    ): Boolean

}