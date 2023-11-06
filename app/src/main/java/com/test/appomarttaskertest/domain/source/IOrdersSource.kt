package com.test.appomarttaskertest.domain.source

import com.test.appomarttaskertest.domain.Order
import com.test.appomarttaskertest.domain.OrderStatus

interface IOrdersSource {

    suspend fun getOrders(): List<Order>

    suspend fun updateOrder(
        id: Int,
        status: OrderStatus
    ) : Boolean

}