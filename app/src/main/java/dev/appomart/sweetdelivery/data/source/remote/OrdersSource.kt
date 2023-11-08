package dev.appomart.sweetdelivery.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import dev.appomart.sweetdelivery.CAKES_COLLECTION_KEY
import dev.appomart.sweetdelivery.domain.Order
import dev.appomart.sweetdelivery.domain.OrderStatus
import dev.appomart.sweetdelivery.domain.source.remote.IOrdersSource
import dev.appomart.sweetdelivery.domain.toInt
import dev.appomart.sweetdelivery.domain.toOrderStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class OrdersSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : IOrdersSource {

    companion object {
        private const val ORDER_ID_KEY = "orderId"
        const val ORDER_NAME_KEY = "orderName"
        private const val ORDER_DESCRIPTION_KEY = "orderDescription"
        private const val ORDER_PRICE_KEY = "orderPrice"
        private const val ORDER_STATUS_KEY = "orderStatus"
        const val ORDER_COORDINATES = "orderCoordinates"
        private const val ORDER_COMMENTARY_KEY = "orderCommentary"
        private const val NULL_STRING = "null"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getOrders(): List<Order> = suspendCancellableCoroutine { continuation ->

        firestore.collection(CAKES_COLLECTION_KEY)
            .get()
            .addOnSuccessListener { result ->
                val documents = result.documents
                if (documents.isNotEmpty()) {
                    val listOfOrders = result.map { it.toOrder() }
                    continuation.resume(listOfOrders, null)
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun updateOrder(
        id: Int,
        status: OrderStatus,
        price: Int?,
        commentary: String?
    ): Boolean = suspendCancellableCoroutine { continuation ->

        val documentRef = firestore.collection(CAKES_COLLECTION_KEY).document(id.toString())

        val updatedValues = hashMapOf<String, Any>()
        updatedValues[ORDER_STATUS_KEY] = status.toInt()
        if (price != null) updatedValues[ORDER_PRICE_KEY] = price
        if (!commentary.isNullOrEmpty()) updatedValues[ORDER_COMMENTARY_KEY] = commentary

        documentRef
            .update(updatedValues)
            .addOnSuccessListener {
                continuation.resume(true, null)
            }
            .addOnFailureListener {
                continuation.resume(false, null)
            }

    }

    private fun QueryDocumentSnapshot.toOrder() = Order(
        id = data[ORDER_ID_KEY].toString().toInt(),
        name = data[ORDER_NAME_KEY].toString(),
        description = data[ORDER_DESCRIPTION_KEY].toString(),
        status = data[ORDER_STATUS_KEY].toString().toInt().toOrderStatus(),
        price = data[ORDER_PRICE_KEY].toString().toInt().getPriceOfOrder(),
        commentary = data[ORDER_COMMENTARY_KEY].toString().getCommentary(),
        longitude = (data[ORDER_COORDINATES] as GeoPoint).longitude,
        latitude = (data[ORDER_COORDINATES] as GeoPoint).latitude
    )

    private fun Int.getPriceOfOrder(): Int? = if (this == -1) null else this

    private fun String.getCommentary(): String? =
        if (this == NULL_STRING || this.isBlank()) null else this

}