package dev.appomart.sweetdelivery.domain.repository

import dev.appomart.sweetdelivery.ui.maps.CoordinatesInfo

interface IMapsRepository {

    suspend fun getMapsInfo(): List<CoordinatesInfo>

}