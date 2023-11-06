package dev.appomart.sweetdelivery.domain.source

import dev.appomart.sweetdelivery.ui.maps.CoordinatesInfo

interface IMapsSource {

    suspend fun getMapsInfo(): List<CoordinatesInfo>

}