package com.test.appomarttaskertest.domain.repository

import com.test.appomarttaskertest.domain.Order

interface IOrderRepository {

    suspend fun getOrders() : List<Order>

    suspend fun updateOrderStatus()

}