package dev.appomart.sweetdelivery.data.repository

import dev.appomart.sweetdelivery.data.source.remote.MapsSource
import dev.appomart.sweetdelivery.domain.repository.IMapsRepository
import dev.appomart.sweetdelivery.ui.maps.CoordinatesInfo
import javax.inject.Inject

class MapsRepository @Inject constructor(
    private val mapsSource: MapsSource
) : IMapsRepository {

    override suspend fun getMapsInfo(): List<CoordinatesInfo> = mapsSource.getMapsInfo()

}