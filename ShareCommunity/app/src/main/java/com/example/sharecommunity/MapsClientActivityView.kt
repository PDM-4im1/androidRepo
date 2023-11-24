package com.example.sharecommunity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.sharecommunity.databinding.ActivityMapsViewBinding
import com.example.sharecommunity.drivers.adapter.DriverAdapter
import com.example.sharecommunity.drivers.services.DriverServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URL
import java.net.URLEncoder
import java.util.Locale

class MapsClientActivityView : AppCompatActivity(),
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsViewBinding
    private var sourceLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    private var currentPolyline: Polyline? = null
    private val markers: MutableList<Marker> = mutableListOf()
    private lateinit var tvDuration: TextView
    private lateinit var showDriversBtn: Button
    private val DELAY_DURATION = 3000
    private val handler = Handler()
    private val driverAdapter = DriverAdapter()

    private val driverServices: DriverServices by lazy {
        createRetrofit().create(DriverServices::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tvDuration = findViewById(R.id.tvDuration)
        showDriversBtn = findViewById(R.id.showdrivers_btn)
        showDriversBtn.setOnClickListener {
            fetchDriverData()
        }
        showDriversBtn.isEnabled = false

        initMap()
        initUI()
    }
    private fun fetchDriverData() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val driversResponse = driverServices.getDrivers().execute()
                val carsResponse = driverServices.getCars().execute()
                val usersResponse = driverServices.getUsers().execute()

                withContext(Dispatchers.Main) {
                    if (driversResponse.isSuccessful && carsResponse.isSuccessful && usersResponse.isSuccessful) {
                        val drivers = driversResponse.body() ?: emptyList()
                        val cars = carsResponse.body() ?: emptyList()
                        val users = usersResponse.body() ?: emptyList()

                        driverAdapter.setData(drivers, users, cars)

                        updateRecyclerView()

                        showDriversBtn.isEnabled = true
                    } else {
                        // Handle unsuccessful responses
                        Toast.makeText(this@MapsClientActivityView, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle exceptions
                    Toast.makeText(this@MapsClientActivityView, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateRecyclerView() {
        setContentView(R.layout.activity_driver_list)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDrivers)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = driverAdapter
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // Replace with your actual backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI() {
        val sourceEditText = binding.etSource
        val destinationEditText = binding.etDestination

        sourceEditText.setOnTouchListener { _, event ->
            val drawableright = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= sourceEditText.right - sourceEditText.compoundDrawables[drawableright].bounds.width()) {
                    handleMyLocationClick(sourceEditText)
                    return@setOnTouchListener true
                }
            }
            false
        }

        sourceEditText.addTextChangedListener {
            // Add a delay before calling updateMap
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                updateMap()
            }, DELAY_DURATION.toLong())
        }

        destinationEditText.addTextChangedListener {
            // Add a delay before calling updateMap
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                updateMap()
            }, DELAY_DURATION.toLong())
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateMap() {
        AsyncTask.execute {
        sourceLocation = getLocationFromAddress(binding.etSource.text.toString())
        destinationLocation = getLocationFromAddress(binding.etDestination.text.toString())

            runOnUiThread {
                if (sourceLocation != null && destinationLocation != null) {
                    getDirections()
                }
            }
    }}
    private fun getDirections() {
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


    private fun handleMyLocationClick(editText: EditText) {
        enableMyLocation()

        mMap.setOnMyLocationChangeListener { location ->
            val myLocation = LatLng(location.latitude, location.longitude)
            sourceLocation = myLocation

            // Use Geocoder to get the address from LatLng
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>?
            try {
                addresses = geocoder.getFromLocation(myLocation.latitude, myLocation.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    editText.setText(address)
                } else {
                    editText.setText("error finding Location")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                editText.setText("error finding Location")
            }

            mMap.setOnMyLocationChangeListener(null)
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
            updateMap()
        }
    }

    private fun addMarkersAndAnimatePolyline(result: DirectionsResult) {
        mMap.clear() // Clear existing markers and polylines

        // Add markers for source and destination
        val sourceMarker = sourceLocation?.let {
            MarkerOptions()
                .position(it)
                .title("Source Location")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(resources, R.drawable.icon8)))
        }?.let {
            mMap.addMarker(
                it
            )
        }

        val destinationMarker = destinationLocation?.let {
            MarkerOptions()
                .position(it)
                .title("Destination Location")
        }?.let {
            mMap.addMarker(
                it
            )
        }

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
            val padding = (resources.displayMetrics.widthPixels * 0.29).toInt() // You can adjust the factor (0.2) as needed
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




    private fun getLocationFromAddress(address: String): LatLng? {
        val apiKey = "AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74"
        val geocodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=${URLEncoder.encode(address, "UTF-8")}&key=$apiKey"

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
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message or handle it accordingly.
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val CAMERA_PADDING = 100
    }

}