package com.example.sharecommunity

import android.content.pm.PackageManager
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sharecommunity.databinding.ActivityMapsDriverviewactivityBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

class MapsDriverviewactivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsDriverviewactivityBinding
    private lateinit var sourceLocation: LatLng
    private lateinit var destinationLocation: LatLng
    private lateinit var etSource: EditText
    private lateinit var etDestination: EditText
    private val markers = mutableListOf<Marker>()
    private var currentPolyline: Polyline? = null
    private lateinit var confirmroutebtn: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsDriverviewactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        etSource = findViewById(R.id.etSourcedriver)
        etDestination = findViewById(R.id.etDestinationdriver)
        sourceLocation = LatLng(0.0, 0.0)
        destinationLocation = LatLng(0.0, 0.0)
        confirmroutebtn = findViewById(R.id.btnConfirmRoute)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapdriverFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        etSource.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateSourceLocation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
        etDestination.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateDestinationLocation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        })
        etSource.setOnTouchListener { _, event ->
            val drawableright = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= etSource.right - etSource.compoundDrawables[drawableright].bounds.width()) {
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun updateSourceLocation(address: String) {
        AsyncTask.execute {
            val newSourceLocation = getLocationFromAddress(address) ?: return@execute

            runOnUiThread {
                sourceLocation = newSourceLocation
                updateMap()
            }
        }
    }

    private fun updateDestinationLocation(address: String) {
        AsyncTask.execute {
            destinationLocation = getLocationFromAddress(address) ?: return@execute
            runOnUiThread {
                updateMap()
            }
        }
    }

    private fun updateMap() {
        AsyncTask.execute {
            runOnUiThread {
                mMap.clear() // Clear existing markers and polylines
            }
            getDirections()
        }
    }

    private fun getDirections() {
        val directionsApi = GeoApiContext.Builder().apiKey("AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74").build()
        val request = DirectionsApi.newRequest(directionsApi)
            .origin(com.google.maps.model.LatLng(sourceLocation.latitude, sourceLocation.longitude))
            .destination(com.google.maps.model.LatLng(destinationLocation.latitude, destinationLocation.longitude))

        try {
            val result = request.await()
            runOnUiThread {
                if (result.routes.isNotEmpty()) {
                    val durationInSeconds = result.routes[0].legs.sumOf { it.duration.inSeconds.toInt() }
                    val durationInMinutes = durationInSeconds / 60
                    val durationText = "$durationInMinutes minutes"
                    showToast(durationText)

                    // Draw the route on the map with animation and markers
                    addMarkersAndAnimatePolyline(result)
                } else {
                    showToast("No routes found")
                }
            }
        } catch (e: Exception) {
            runOnUiThread {
                e.printStackTrace()
                showToast("Error getting directions")
            }
        }
    }


    private fun addMarkersAndAnimatePolyline(result: DirectionsResult) {
        mMap.clear() // Clear existing markers and polylines

        // Add markers for source and destination
        val sourceMarker = mMap.addMarker(
            MarkerOptions()
                .position(sourceLocation)
                .title("Source Location")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.icon8)))
        )

        val destinationMarker = mMap.addMarker(
            MarkerOptions()
                .position(destinationLocation)
                .title("Destination Location")
        )

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
                        .color(Color.BLUE) // You can use Color.GREEN if you prefer green
                        .width(15f) // Adjust the width as needed
                        .startCap(RoundCap())
                        .endCap(RoundCap())
                        .jointType(JointType.ROUND)
                        .pattern(listOf(Dot(), Gap(10f)))



                    val points = step.polyline.decodePath()
                    for (point in points) {
                        polylineOptions.add(LatLng(point.lat, point.lng))
                    }

                    currentPolyline = mMap.addPolyline(polylineOptions)
                }
            }

            // Move camera to the bounds of the route
            val boundsBuilder = LatLngBounds.builder()
            for (point in currentPolyline!!.points) {
                boundsBuilder.include(point)
            }

            val bounds = boundsBuilder.build()

            // Set camera position to show the entire route with a padding
            val padding = (resources.displayMetrics.widthPixels * 0.25).toInt() // You can adjust the factor (0.2) as needed
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding), 2000, null)

            // Set a top-down perspective
            val tilt = 90.0f
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding, padding,
                tilt.toInt()
            ))
            // Animate the polyline
        }
    }

    private fun showToast(message: String) {
            Toast.makeText(this@MapsDriverviewactivity, message, Toast.LENGTH_SHORT).show()

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

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()

        // Set the source EditText with the current location
        etSource.setText("Latitude: ${location.latitude}, Longitude: ${location.longitude}")
    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Display rationale if needed
            Toast.makeText(this, "Location permission required for this feature", Toast.LENGTH_SHORT).show()
            return
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != 1) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message or handle it accordingly.
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val CAMERA_PADDING = 100
    }
}

