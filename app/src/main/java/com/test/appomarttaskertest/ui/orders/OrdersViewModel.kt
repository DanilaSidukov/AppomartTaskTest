package com.test.appomarttaskertest.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.appomarttaskertest.domain.Order
import com.test.appomarttaskertest.domain.repository.IOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: IOrderRepository
): ViewModel() {

    private val _listOfOrders: MutableStateFlow<List<Order>> = MutableStateFlow(emptyList())
    var listOfOrders = _listOfOrders.asStateFlow()

    init {
        viewModelScope.launch {
            getOrderList()
        }
    }

    private suspend fun getOrderList() {
        _listOfOrders.emit(
            ordersRepository.getOrders()
        )
    }

}