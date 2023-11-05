package com.test.appomarttaskertest.domain

sealed interface OrderStatus {
    object New : OrderStatus
    object InProgress: OrderStatus
    object Canceled: OrderStatus
    object Finished: OrderStatus
    object Closed: OrderStatus
}

fun OrderStatus.toInt() = when(this) {
    OrderStatus.Canceled -> 3
    OrderStatus.Closed -> 5
    OrderStatus.Finished -> 4
    OrderStatus.InProgress -> 2
    OrderStatus.New -> 1
}

fun Int.toOrderStatus() = when(this) {
    1 -> OrderStatus.New
    2 -> OrderStatus.InProgress
    3 -> OrderStatus.Canceled
    4 -> OrderStatus.Finished
    else -> OrderStatus.Closed
}