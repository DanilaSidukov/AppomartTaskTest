package dev.appomart.sweetdelivery.ui.orders

import androidx.recyclerview.widget.DiffUtil
import dev.appomart.sweetdelivery.domain.Order

class OrderDiffCallback(
    private val oldOrderList: List<Order>,
    private val newOrderList: List<Order>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldOrderList.size
    }

    override fun getNewListSize(): Int {
        return newOrderList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldOrderList[oldItemPosition].name === newOrderList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOrder: Order = oldOrderList[oldItemPosition]
        val newOrder: Order = newOrderList[newItemPosition]
        return oldOrder.name == newOrder.name
    }

}