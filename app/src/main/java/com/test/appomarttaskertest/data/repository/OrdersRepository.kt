package com.test.appomarttaskertest.data.repository

import com.test.appomarttaskertest.domain.repository.IOrderRepository
import com.test.appomarttaskertest.domain.source.IOrdersSource
import javax.inject.Inject

class OrdersRepository @Inject constructor(
    private val ordersSource: IOrdersSource
) : IOrderRepository {

    override suspend fun getOrders() = ordersSource.getOrders()

    override suspend fun updateOrderStatus() {

    }

}