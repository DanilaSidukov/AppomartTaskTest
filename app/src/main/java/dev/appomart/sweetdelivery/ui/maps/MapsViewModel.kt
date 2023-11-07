package dev.appomart.sweetdelivery.ui.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.appomart.sweetdelivery.domain.repository.IMapsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoordinatesInfo(
    val latitude: Double,
    val longitude: Double,
    val name: String
)

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsRepository: IMapsRepository
) : ViewModel() {

    private var _coordinatesList = MutableStateFlow<List<CoordinatesInfo>>(emptyList())
    var coordinatesList = _coordinatesList.asStateFlow()

    init {
        getCoordinatesList()
    }

    private fun getCoordinatesList() {
        viewModelScope.launch {
            _coordinatesList.emit(
                mapsRepository.getMapsInfo()
            )
        }
    }

}