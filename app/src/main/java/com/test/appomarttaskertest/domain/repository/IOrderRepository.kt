package com.test.appomarttaskertest.domain.repository

import com.test.appomarttaskertest.domain.Order
import com.test.appomarttaskertest.domain.OrderStatus

interface IOrderRepository {

    suspend fun getOrders() : List<Order>

    suspend fun updateOrderStatus(
        id: Int,
        status: OrderStatus
    ) : Boolean

}