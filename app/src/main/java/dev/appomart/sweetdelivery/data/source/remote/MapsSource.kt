package dev.appomart.sweetdelivery.data.source.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import dev.appomart.sweetdelivery.data.source.local.Settings.CAKES_COLLECTION_KEY
import dev.appomart.sweetdelivery.data.source.remote.OrdersSource.Companion.ORDER_NAME_KEY
import dev.appomart.sweetdelivery.domain.source.IMapsSource
import dev.appomart.sweetdelivery.ui.maps.CoordinatesInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class MapsSource @Inject constructor(
    private val firestore: FirebaseFirestore
): IMapsSource {
    override suspend fun getMapsInfo(): List<CoordinatesInfo> = suspendCancellableCoroutine { continuation ->

        firestore.collection(CAKES_COLLECTION_KEY).get().addOnSuccessListener { result ->
            val documents = result.documents
            if (documents.isNotEmpty()) {
                val mapInfoList = result.map { it.toCoordinatesInfo() }
                continuation.resume(mapInfoList, null)
            }
        }
    }

    private fun QueryDocumentSnapshot.toCoordinatesInfo() = CoordinatesInfo(
        longitude = (data[OrdersSource.ORDER_COORDINATES] as GeoPoint).longitude,
        latitude = (data[OrdersSource.ORDER_COORDINATES] as GeoPoint).latitude,
        name = data[ORDER_NAME_KEY].toString()
    )


}