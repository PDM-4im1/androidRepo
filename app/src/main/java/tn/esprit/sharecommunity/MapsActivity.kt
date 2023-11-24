package tn.esprit.sharecommunity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import org.json.JSONObject
import tn.esprit.sharecommunity.databinding.ActivityMapsBinding
import tn.esprit.sharecommunity.drivers.adapter.EmDriverAdapter
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.Locale
import kotlin.math.min

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
GoogleMap.OnMyLocationClickListener,
ActivityCompat.OnRequestPermissionsResultCallback {


    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var sourceLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    private var currentPolyline: Polyline? = null
    private val markers: MutableList<Marker> = mutableListOf()
    private lateinit var tvDuration: TextView
    private lateinit var showDriversBtn: Button
    private val DELAY_DURATION = 3000
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        initMap()
        //initUI()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


       fun getDirections() {
            val directionsApi = GeoApiContext.Builder().apiKey("AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74").build()
            val request = DirectionsApi.newRequest(directionsApi)
                .origin(com.google.maps.model.LatLng(sourceLocation!!.latitude, sourceLocation!!.longitude))
                .destination(com.google.maps.model.LatLng(destinationLocation!!.latitude, destinationLocation!!.longitude))

            try {
                val result = request.await()
                if (result.routes.isNotEmpty()) {
                    val durationInSeconds =
                        result.routes[0].legs.sumOf { it.duration.inSeconds.toInt() }
                    val durationInMinutes = durationInSeconds / 60
                    val durationText = "$durationInMinutes minutes"
                    tvDuration.text = durationText // Update the TextView
                    showToast(durationText)
                    showDriversBtn.isEnabled = true

                    // Draw the route on the map with animation and markers
                    addMarkersAndAnimatePolyline(result)
                } else {
                    showDriversBtn.isEnabled = false
                    showToast("No routes found")
                }
            } catch (e: Exception) {
                showDriversBtn.isEnabled = false
                e.printStackTrace()
                showToast("Error getting directions")
            }
        }

    private fun getLocationFromAddress(address: String): LatLng? {
        val apiKey = "AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74"
        val geocodingUrl =
            "https://maps.googleapis.com/maps/api/geocode/json?address=${URLEncoder.encode(address, "UTF-8")}&key=$apiKey"

        try {
            val response = URL(geocodingUrl).readText()
            val jsonObj = JSONObject(response)

            if (jsonObj.getString("status") == "OK") {
                val results = jsonObj.getJSONArray("results")
                if (results.length() > 0) {
                    val location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location")
                    val lat = location.getDouble("lat")
                    val lng = location.getDouble("lng")
                    return LatLng(lat, lng)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun updateRecyclerView(context : Context, driverAdapter : EmDriverAdapter) {
        setContentView(R.layout.fragment_emgcovoiturage)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEMDrivers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = driverAdapter
    }
        fun handleMyLocationClick(context: Context, editText: EditText) {
            enableMyLocation(context,mMap)

            this.mMap.setOnMyLocationButtonClickListener {
                // Get the current location
                val location = mMap.myLocation
                if (location != null) {
                    val myLocation = LatLng(location.latitude, location.longitude)
                    sourceLocation = myLocation

                    // Use Geocoder to get the address from LatLng
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses: List<Address>?
                    try {
                        addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0].getAddressLine(0)
                            editText.setText(address)
                        } else {
                            editText.setText("Error finding location")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        editText.setText("Error finding location")
                    }

                    // Return true to indicate that the listener has consumed the event
                    true
                } else {

                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editText.windowToken, 0)
                    // updateMap()

                    false
                }
            }
        }

        private fun addMarkersAndAnimatePolyline(result: DirectionsResult) {
            mMap.clear() // Clear existing markers and polylines

            // Add markers for source and destination
            val sourceMarker = mMap.addMarker(MarkerOptions().position(sourceLocation!!).title("Source Location"))
            val destinationMarker = mMap.addMarker(MarkerOptions().position(destinationLocation!!).title("Destination Location"))

            if (sourceMarker != null) {
                markers.add(sourceMarker)
            }
            if (destinationMarker != null) {
                markers.add(destinationMarker)
            }

            // Draw the route
            if (result.routes.isNotEmpty()) {
                val route = result.routes[0]
                val legs = route.legs
                for (leg in legs) {
                    val steps = leg.steps
                    for (i in steps.indices) {
                        val step = steps[i]
                        val polylineOptions = PolylineOptions()
                            .color(Color.RED)
                            .width(5f)

                        val points = step.polyline.decodePath()
                        for (point in points) {
                            polylineOptions.add(LatLng(point.lat, point.lng))
                        }

                        currentPolyline = mMap.addPolyline(polylineOptions)

                        // Add markers for each step
                        val marker = mMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(step.startLocation.lat, step.startLocation.lng))
                                .title("Step $i")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        )
                        if (marker != null) {
                            markers.add(marker)
                        }
                    }
                }

                // Move camera to the bounds of the route
                val boundsBuilder = LatLngBounds.builder()
                for (point in currentPolyline!!.points) {
                    boundsBuilder.include(point)
                }

                val bounds = boundsBuilder.build()
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, CAMERA_PADDING))

                // Animate the polyline
            }
        }

        private fun animatePolyline() {
            val handler = Handler(Looper.getMainLooper())
            val totalPoints = currentPolyline?.points?.size ?: 0

            if (totalPoints > 0) {
                val animator = ValueAnimator.ofInt(0, totalPoints - 1)
                animator.duration = 2000 // Adjust the duration as needed

                animator.addUpdateListener { valueAnimator ->
                    val animatedValue = valueAnimator.animatedValue as Int
                    val endIndex = min(animatedValue + 1, totalPoints)
                    currentPolyline?.points = currentPolyline?.points?.subList(0, endIndex)!!
                }

                animator.start()

                handler.postDelayed({
                    // Remove markers after animation completes
                    for (marker in markers) {
                        marker.remove()
                    }
                }, animator.duration)
            }
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }





        override fun onMapReady(googleMap: GoogleMap) {
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val sydney = LatLng(-34.0, 151.0)
            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }


        /**
         * Enables the My Location layer if the fine location permission has been granted.
         */
        fun enableMyLocation(context: Context, Map : GoogleMap) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Map.isMyLocationEnabled = true
                return
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                // Display rationale if needed
                Toast.makeText(context, "Location permission required for this feature", Toast.LENGTH_SHORT).show()
                return
            }

            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        override fun onMyLocationButtonClick(): Boolean {
            Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
            return false
        }

        override fun onMyLocationClick(location: Location) {
            Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                super.onRequestPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults
                )
                return
            }

            if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                enableMyLocation(this,mMap)
            } else {
                // Permission was denied. Display an error message or handle it accordingly.
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        companion object {
            const val LOCATION_PERMISSION_REQUEST_CODE = 1
            private const val CAMERA_PADDING = 100
        }

    }