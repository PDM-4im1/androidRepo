package tn.esprit.sharecommunity

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {
    private lateinit var mMap: GoogleMap
    private var sourceLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initMap()

        return inflater.inflate(R.layout.fragment_map, container, false)
    }
    private fun initMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.Dmap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
       // var myLocation: LatLng
        //myLocation = LatLng(-34.0, 151.0)
            // Assuming mMap is a class variable or obtained in your code
            mMap = googleMap
        mMap.setOnMyLocationChangeListener { location ->
            // Add a marker with a red icon at a specific location
            var myLocation = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(myLocation).title("I am here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red)))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        }

      //  mMap.addMarker(MarkerOptions().position(myLocation).title("I am here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red)))



    }

    override fun onMyLocationButtonClick(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("Not yet implemented")
    }
}