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
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import dev.appomart.sweetdelivery.R
import dev.appomart.sweetdelivery.databinding.FragmentMapsBinding
import dev.appomart.sweetdelivery.ui.orders.OrdersViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var mMap: GoogleMap

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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewLifecycleOwner.lifecycleScope.launch {
            mapsViewModel.coordinatesList.collect { position ->
                if (position.isEmpty()) return@collect
                position.forEach { info ->
                    val coordinates = LatLng(info.latitude, info.longitude)
                    println("coord = $coordinates")
                    mMap.addMarker(
                        MarkerOptions()
                            .position(coordinates)
                            .title(info.name)
                    )
                }
                val firstPosition = LatLng(position[0].latitude, position[0].longitude)
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(firstPosition))
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(
//            MarkerOptions()
//            .position(sydney)
//            .title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(mMap.cameraPosition))
    }
}