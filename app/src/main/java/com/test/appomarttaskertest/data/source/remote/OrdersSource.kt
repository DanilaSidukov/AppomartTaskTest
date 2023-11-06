package com.test.appomarttaskertest.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.test.appomarttaskertest.domain.Order
import com.test.appomarttaskertest.domain.OrderStatus
import com.test.appomarttaskertest.domain.source.IOrdersSource
import com.test.appomarttaskertest.domain.toInt
import com.test.appomarttaskertest.domain.toOrderStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class OrdersSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : IOrdersSource {

    private companion object {
        const val CAKES_COLLECTION_KEY = "cakes"
        private const val ORDER_ID_KEY = "orderId"
        private const val ORDER_NAME_KEY = "orderName"
        private const val ORDER_DESCRIPTION_KEY = "orderDescription"
        private const val ORDER_PRICE_KEY = "orderPrice"
        private const val ORDER_STATUS_KEY = "orderStatus"
        private const val ORDER_COORDINATES = "orderCoordinates"
        private const val ORDER_COMMENTARY_KEY = "orderCommentary"
        private const val NULL_STRING = "null"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getOrders(): List<Order> = suspendCancellableCoroutine { continuation ->
        firestore.collection(CAKES_COLLECTION_KEY).get().addOnSuccessListener { result ->
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
        status: OrderStatus
    ) : Boolean = suspendCancellableCoroutine { continuation ->
        val documentRef = firestore.collection(CAKES_COLLECTION_KEY).document(id.toString())
        documentRef
            .update(ORDER_STATUS_KEY, status.toInt())
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

    private fun Int.getPriceOfOrder() : Int? = if (this == -1) null else this

    private fun String.getCommentary(): String? =
        if (this == NULL_STRING || this.isBlank()) null else this

}