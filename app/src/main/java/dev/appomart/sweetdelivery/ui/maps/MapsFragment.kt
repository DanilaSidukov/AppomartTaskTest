package dev.appomart.sweetdelivery.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.databinding.FragmentMapsBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var googleMap: GoogleMap

    private companion object {
        const val ZOOM_VALUE = 12.0f
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            mapsViewModel.coordinatesList.collect { position ->
                if (position.isEmpty() || !this@MapsFragment::googleMap.isInitialized) return@collect
                position.forEach { info ->
                    val coordinates = LatLng(info.latitude, info.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(coordinates)
                            .title(info.name)
                    )
                }
                val firstPosition = LatLng(position[0].latitude, position[0].longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(firstPosition))
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(ZOOM_VALUE))
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

    }
}