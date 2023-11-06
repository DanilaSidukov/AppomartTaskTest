package dev.appomart.sweetdelivery.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.GeoPoint
import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.domain.repository.IOrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
): ViewModel() {

    private var _uiState = MutableStateFlow(OrdersUiState())
    val uiState = _uiState.asStateFlow()

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
        val statusList = when(currentStatus) {
            OrderStatus.Canceled, OrderStatus.Closed -> emptyList()
            OrderStatus.Finished -> listOf(OrderStatus.Closed)
            OrderStatus.InProgress -> listOf(OrderStatus.Finished, OrderStatus.Canceled)
            OrderStatus.New -> listOf(OrderStatus.InProgress, OrderStatus.Canceled)
        }
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