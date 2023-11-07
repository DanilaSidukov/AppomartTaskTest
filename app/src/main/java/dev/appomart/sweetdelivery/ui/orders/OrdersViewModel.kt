package dev.appomart.sweetdelivery.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.domain.repository.IOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrdersUiState(
    val orderList: List<Order> = emptyList(),
    val availableStatuses: List<OrderStatus> = emptyList(),
    val canEditStatus: Boolean? = null,
    val currentOrderId: Int = 0,
    val isUpdateSuccessful: Boolean? = null,
    val showLoader: Boolean = true
)

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val ordersRepository: IOrderRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

    private companion object {
        private val updateOrderMap = mapOf(
            OrderStatus.Canceled to emptyList(),
            OrderStatus.Closed to emptyList(),
            OrderStatus.Finished to listOf(OrderStatus.Closed),
            OrderStatus.InProgress to listOf(OrderStatus.Finished, OrderStatus.Canceled),
            OrderStatus.New to listOf(OrderStatus.InProgress, OrderStatus.Canceled)
        )
    }


    init {
        getOrderList()
    }

    private fun getOrderList() {
        viewModelScope.launch {
            val orders = ordersRepository.getOrders()
            _uiState.emit(
                _uiState.value.copy(
                    orderList = orders,
                    showLoader = false
                )
            )
        }
    }

    fun requestStatusList(id: Int, currentStatus: OrderStatus) {
        val statusList = updateOrderMap[currentStatus] ?: return
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    availableStatuses = statusList,
                    canEditStatus = statusList.isNotEmpty(),
                    currentOrderId = id,
                    isUpdateSuccessful = null
                )
            )
        }
    }

    fun clearEditStatus() {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    canEditStatus = null
                )
            )
        }
    }

    fun updateStatus(id: Int, status: OrderStatus, price: Int?, commentary: String?) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    showLoader = true,
                    canEditStatus = null
                )
            )
            val isSuccessful = ordersRepository.updateOrderStatus(id, status, price, commentary)
            val updatedList = ordersRepository.getOrders()
            _uiState.emit(
                _uiState.value.copy(
                    orderList = updatedList,
                    isUpdateSuccessful = isSuccessful,
                    showLoader = false
                )
            )
        }
    }

}