package dev.appomart.sweetdelivery.data.repository

import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.domain.repository.IOrderRepository
import dev.appomart.sweetdelivery.domain.source.IOrdersSource
import javax.inject.Inject

class OrdersRepository @Inject constructor(
    private val ordersSource: IOrdersSource
) : IOrderRepository {

    override suspend fun getOrders() = ordersSource.getOrders()

    override suspend fun updateOrderStatus(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    ) = ordersSource.updateOrder(id, status, price, commentary)

}