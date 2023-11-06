package dev.appomart.sweetdelivery.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.ui.asString
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.databinding.ItemOrderBinding

class OrdersAdapter(
    private var orderList: List<Order>,
    private val listener: OnChangeOrderStatusListener,
): RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val binding : ItemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orderList[position]
        holder.bindItem(order)
        holder.openStatusDialog(listener, order)
    }

    class OrdersViewHolder(private val binding: ItemOrderBinding): RecyclerView.ViewHolder(binding.root) {

        private val priceNotAssigned =
            binding.root.context.getString(R.string.label_price_not_assigned)
        private val commentaryNotAssigned =
            binding.root.context.getString(R.string.label_commentary_not_assigned)

        fun bindItem(order: Order) {
            val status = order.status.asString(itemView.context)
            with(binding){
                textOrderId.text = itemView.context.getString(R.string.label_order_id, order.id)
                textOrderName.text = itemView.context.getString(R.string.label_order_name, order.name)
                textOrderDescription.text = itemView.context.getString(R.string.label_order_description, order.description)
                orderPrice.text = order.price?.toString() ?: priceNotAssigned
                orderStatus.text = status
                textOrderCommentary.text = itemView.context.getString(
                    R.string.label_order_commentary,
                    order.commentary ?: commentaryNotAssigned
                )

            }
        }
        fun openStatusDialog(listener: OnChangeOrderStatusListener, order: Order) {
            binding.changeOrderStatus.post {
                binding.changeOrderStatus.setOnClickListener {
                    listener.onChangeOrderStatusListener(order.id, order.status)
                }
            }
        }

    }

    fun updateList(newList: List<Order>){
        val orderDiffCallback = OrderDiffCallback(orderList, newList)
        val diffResult = DiffUtil.calculateDiff(orderDiffCallback)
        orderList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = orderList.size

}

interface OnChangeOrderStatusListener{
    fun onChangeOrderStatusListener(orderId: Int, orderStatus: OrderStatus)
}