package com.test.appomarttaskertest.domain.source

import com.test.appomarttaskertest.domain.Order

interface IOrdersSource {

    suspend fun getOrders() : List<Order>

}