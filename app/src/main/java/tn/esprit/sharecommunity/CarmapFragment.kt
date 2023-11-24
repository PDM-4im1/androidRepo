package tn.esprit.sharecommunity

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.sharecommunity.cars.data.CarData
import tn.esprit.sharecommunity.databinding.FragmentCarmapBinding
import tn.esprit.sharecommunity.databinding.FragmentDirectionBinding
import tn.esprit.sharecommunity.drivers.adapter.EmDriverAdapter
import tn.esprit.sharecommunity.drivers.data.DriverData
import tn.esprit.sharecommunity.drivers.data.UserData
import java.io.IOException
import java.util.Locale

class CarmapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback {
     val MapsActivity = MapsActivity()
    val EmgActivity = EmgCovoiturageActivity()
    private lateinit var mMap: GoogleMap
    private var sourceLocation: LatLng? = null
    private var destinationLocation: LatLng? = null
    private lateinit var binding: FragmentCarmapBinding
    private lateinit var Directionbinding: FragmentDirectionBinding
    private val PLACE_PICKER_REQUEST = 1
    private var currentPolyline: Polyline? = null
    private val markers: MutableList<Marker> = mutableListOf()
    private val driverAdapter = EmDriverAdapter()

    private val staticDrivers = listOf(
        DriverData(id_cond = 5,id_moyen_transpor = 1, id_user = 1, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),
        DriverData(id_cond = 6,id_moyen_transpor = 1, id_user = 2, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),
        DriverData(id_cond = 7,id_moyen_transpor = 1, id_user = 3, pointDepart="A",pointArrivee="B", localisation = "YourLocation"),

        )

    private val staticUsers = listOf(
        UserData( id_user = 1, email="aaa.vvv@ggg.cc" ,password = "aaaaaa",Phone_number = 22555999 ,role ="Driver",name="mizo", first_name = "John",  age = 22),
        UserData( id_user = 2, email="zzzz.gggg@vvv.cc" ,password = "sssss",Phone_number = 22478596 ,role ="Driver", first_name = "Dai", name = "Doe", age = 25),
        UserData( id_user = 3, email="hhh.fff@nn.cc" ,password = "pppp",Phone_number = 20147852 ,role ="Driver",first_name = "dido", name = "mansour", age = 24),

        )

    private val staticCars = listOf(
        CarData(id_moyen_transpor = 1, marque = "Toyota", type = "vehicule",matricule = "123TN7895",image = "aaaa",trajet = "15746",idConducteur = 5
        ),
    )

   // private val DriversBtn = binding.btnRide

   /* private val driverServices:
            EmDriverService by lazy {
        createRetrofit().create(EmDriverService::class.java)
    }*/


    private lateinit var placesClient: PlacesClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        Places.initialize(requireContext(), "AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74")
        binding = FragmentCarmapBinding.inflate(inflater, container, false)
        placesClient = Places.createClient(requireContext())
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val locationEditText = view.findViewById<TextView>(R.id.locationEditText)
        val searchEditText =  binding.searchEditText
        val btnShowHospitals = binding.hospitalBtn
        val btnShowPahrmacy = binding.pharmacyBtn
        val btnShowPolices = binding.policeStationBtn
        val btnShowDoctor = binding.doctorBtn
        val btnShowRide = binding.btnRide
            //.findViewById<EditText>(R.id.searchEditText)
       // val searchLayout = view.findViewById<LinearLayout>(R.id.searchLayout)
        btnShowHospitals.setOnClickListener {
            showHospitalsInCountry()
        }

        btnShowPahrmacy.setOnClickListener{
        showPharmacysInCountry()
        }
        btnShowPolices.setOnClickListener{
            showPolicesInCountry()
        }
        btnShowDoctor.setOnClickListener{
            showDoctorsInCountry()
        }
      /*  locationEditText.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container, EmgCovoiturageFragment())
                addToBackStack(null)
            }
        }*/
        btnShowRide.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container, EmgCovoiturageFragment())
                addToBackStack(null)
            }
            EmgActivity.fetchDriverData()

        }
        locationEditText.setOnClickListener {
            openAutocompleteActivity()
        }

        val sourceBTN = binding.btnPosition
        sourceBTN.setOnClickListener {
            handleMyLocationClick(requireContext(), searchEditText)

        }


    }

    private fun handleMyLocationClick(context: Context, editText: EditText) {
        MapsActivity.enableMyLocation(context,mMap)

        mMap.setOnMyLocationChangeListener { location ->
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
                    editText.setText("error finding Location")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                editText.setText("error finding Location")
            }

            mMap.setOnMyLocationChangeListener(null)
            //val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            //imm.hideSoftInputFromWindow(editText.windowToken, 0)
            //updateMap()
        }
        }

    private fun openAutocompleteActivity() {
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(requireContext())

        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }
    private fun placeMarkerOnMap(latLng: LatLng) {
        // Add a marker to the map
        mMap.addMarker(MarkerOptions().position(latLng).title("Selected Place"))
        // Optionally, move the camera to the selected place
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mMap.clear()
        super.onActivityResult(requestCode, resultCode, data)
        var locationEditText = binding.searchEditText
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val originLatLng: LatLng? = if (locationEditText.text.isNullOrEmpty()) {
                    getCurrentLocation()
                } else {
                    sourceLocation
                }

                val place = Autocomplete.getPlaceFromIntent(data!!)
                placeMarkerOnMap(place.latLng)
                moveCamera(place.latLng)
                if (originLatLng != null) {
                    destinationLocation = place.latLng
                    traceRoute(originLatLng, place.latLng)
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                // Handle error
                Log.e("AutocompleteError", status.statusMessage ?: "Error")
            }
        }
    }
    private fun traceRoute(origin: LatLng, destination: LatLng) {
        val directionsApi = GeoApiContext.Builder().apiKey("AIzaSyCmXUO6nmL7sbV1Z6UEysVERFQUtQj6i74").build()
        val request = DirectionsApi.newRequest(directionsApi)
            .origin(com.google.maps.model.LatLng(sourceLocation!!.latitude, sourceLocation!!.longitude))
            .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
        val result = request.await()

        mMap.clear() // Clear existing markers and polylines

        // Add markers for source and destination
        val sourceMarker = mMap.addMarker(MarkerOptions().position(sourceLocation!!).title("Source Location"))
        val destinationMarker = mMap.addMarker(MarkerOptions().position(destination!!).title("Destination Location"))

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
                    /*val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(step.startLocation.lat, step.startLocation.lng))
                            .title("Step $i")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    )
                    if (marker != null) {
                        markers.add(marker)
                    }*/
                }
            }

            // Move camera to the bounds of the route
            val boundsBuilder = LatLngBounds.builder()
            for (point in currentPolyline!!.points) {
                boundsBuilder.include(point)
            }

            val bounds = boundsBuilder.build()
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,CAMERA_PADDING))

            // Animate the polyline
            val locationEditText = binding.locationEditText

            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses: List<Address>? =
                geocoder.getFromLocation(destination.latitude, destination.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0].getAddressLine(0)
                locationEditText.setText(address)
            }
            //duration
            val durationInSeconds =
                result.routes[0].legs.sumOf { it.duration.inSeconds.toInt() }
            val durationInMinutes = durationInSeconds / 60
            val durationText = "$durationInMinutes minutes"
            var tvDuration = binding.tvDuration
            tvDuration.text  = durationText

                //price
            // Assuming your rate per kilometer is 1.0 (you should replace this with your actual rate)
            val ratePerKilometer = 1.0

// Calculate the total distance in kilometers
            val totalDistanceInMeters = result.routes[0].legs.sumOf { it.distance.inMeters.toInt() }
            val totalDistanceInKilometers = totalDistanceInMeters / 1000.0

// Calculate the total price
            val totalPrice = ratePerKilometer * totalDistanceInKilometers

// Display the total price
            val tvPrice = binding.tvPrice
            tvPrice.text = "Total Price: $$totalPrice"

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getCurrentLocation(): LatLng? {
        MapsActivity.enableMyLocation(requireContext(),mMap)
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        sourceLocation = LatLng(location.latitude, location.longitude)


                    }
                }
        } catch (securityException: SecurityException) {
            // Handle the exception
            securityException.printStackTrace()
        }

        return sourceLocation // Return a default value if there's an issue obtaining the location
    }

    private fun getLocationFromEditText(): LatLng {
        var locationEditText = binding.searchEditText
        return locationEditText.text.toString().split(",").run {
            if (size == 2) {
                LatLng(get(0).toDouble(), get(1).toDouble())
            } else {
                LatLng(0.0, 0.0)
            }
        }
    }
    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        // You can customize the zoom level as needed
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

   /* private fun updateMap() {
        sourceLocation = MapsActivity.getLocationFromAddress(binding.searchEditText.text.toString())
        destinationLocation = MapsActivity.getLocationFromAddress(Directionbinding.DestinationEditText.text.toString())

        if (sourceLocation != null && destinationLocation != null) {
            MapsActivity.getDirections()
        }
    }*/
    @SuppressLint("PotentialBehaviorOverride")
    private fun showHospitalsInCountry() {
        mMap.clear()
        // Specify the fields to return (in this case, just the place name and location)
        val placeFields: List<Field> = listOf(Field.NAME, Field.LAT_LNG)

        // Get the country and region information using Geocoding API
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            // Replace "YOUR_LATITUDE" and "YOUR_LONGITUDE" with the actual latitude and longitude
            addresses = geocoder.getFromLocation(sourceLocation!!.latitude, sourceLocation!!.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val country = addresses[0].countryName
                val region = addresses[0].adminArea

                // Create a FindAutocompletePredictionsRequest with type filter for hospitals
                val request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery("hospital $country") // Search query for hospitals with country and region
                    .build()

                // Perform the Autocomplete request
                val autocompleteResponse = placesClient.findAutocompletePredictions(request)

                autocompleteResponse.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = task.result

                        // Process the Autocomplete response
                        response?.autocompletePredictions?.forEach { prediction ->
                            // Retrieve details for each place
                            val placeRequest = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()

                            placesClient.fetchPlace(placeRequest)
                                .addOnCompleteListener { innerTask ->
                                    if (innerTask.isSuccessful) {
                                        val place = innerTask.result?.place
                                        // Access place details (e.g., place.name, place.latLng)
                                        if (place != null) {
                                            val name = place.name
                                            val latLng = place.latLng

                                            // Handle the retrieved information as needed
                                            Log.d("PlaceInfo", "Name: $name, LatLng: $latLng")
                                            val marker = mMap.addMarker(
                                                MarkerOptions()
                                                    .position(latLng)
                                                    .title(place.name)
                                                    .snippet(place.address)
                                            )
                                            mMap.setOnMarkerClickListener { marker ->
                                                // Handle marker click here
                                                // Retrieve the LatLng from the marker's tag
                                                val destination = latLng

                                                // Call traceRoute with sourceLocation and destinationLatLng
                                                traceRoute(sourceLocation!!, marker.position!!)
                                                true
                                            }


                                        }
                                    } else {
                                        // Handle innerTask exception
                                        val exception = innerTask.exception
                                        exception?.printStackTrace()
                                    }
                                }

                        }
                    } else {
                        // Handle task exception
                        val exception = task.exception
                        exception?.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }



    }
    @SuppressLint("PotentialBehaviorOverride")
    private fun showPolicesInCountry() {
        mMap.clear()
        // Specify the fields to return (in this case, just the place name and location)
        val placeFields: List<Field> = listOf(Field.NAME, Field.LAT_LNG)

        // Get the country and region information using Geocoding API
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            // Replace "YOUR_LATITUDE" and "YOUR_LONGITUDE" with the actual latitude and longitude
            addresses = geocoder.getFromLocation(sourceLocation!!.latitude, sourceLocation!!.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val country = addresses[0].countryName
                val region = addresses[0].adminArea

                // Create a FindAutocompletePredictionsRequest with type filter for hospitals
                val request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery("police station $country") // Search query for hospitals with country and region
                    .build()

                // Perform the Autocomplete request
                val autocompleteResponse = placesClient.findAutocompletePredictions(request)

                autocompleteResponse.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = task.result

                        // Process the Autocomplete response
                        response?.autocompletePredictions?.forEach { prediction ->
                            // Retrieve details for each place
                            val placeRequest = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()

                            placesClient.fetchPlace(placeRequest)
                                .addOnCompleteListener { innerTask ->
                                    if (innerTask.isSuccessful) {
                                        val place = innerTask.result?.place
                                        // Access place details (e.g., place.name, place.latLng)
                                        if (place != null) {
                                            val name = place.name
                                            val latLng = place.latLng
                                            destinationLocation = latLng
                                            // Handle the retrieved information as needed
                                            Log.d("PlaceInfo", "Name: $name, LatLng: $latLng")
                                            val marker = mMap.addMarker(
                                                MarkerOptions()
                                                    .position(latLng)
                                                    .title(place.name)
                                                    .snippet(place.address)
                                            )



                                        }
                                    } else {
                                        // Handle innerTask exception
                                        val exception = innerTask.exception
                                        exception?.printStackTrace()
                                    }
                                }
                            mMap.setOnMarkerClickListener { marker ->
                                // Handle marker click here
                                // Retrieve the LatLng from the marker's tag

                                // Call traceRoute with sourceLocation and destinationLatLng
                                traceRoute(sourceLocation!!,  marker.position!!)
                                true
                            }
                        }
                    } else {
                        // Handle task exception
                        val exception = task.exception
                        exception?.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }



    }
    @SuppressLint("PotentialBehaviorOverride")
    private fun showPharmacysInCountry() {
        mMap.clear()
        // Specify the fields to return (in this case, just the place name and location)
        val placeFields: List<Field> = listOf(Field.NAME, Field.LAT_LNG)

        // Get the country and region information using Geocoding API
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            // Replace "YOUR_LATITUDE" and "YOUR_LONGITUDE" with the actual latitude and longitude
            addresses = geocoder.getFromLocation(sourceLocation!!.latitude, sourceLocation!!.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val country = addresses[0].countryName
                val region = addresses[0].adminArea

                // Create a FindAutocompletePredictionsRequest with type filter for hospitals
                val request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery("pharmacy $country") // Search query for hospitals with country and region
                    .build()

                // Perform the Autocomplete request
                val autocompleteResponse = placesClient.findAutocompletePredictions(request)

                autocompleteResponse.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = task.result

                        // Process the Autocomplete response
                        response?.autocompletePredictions?.forEach { prediction ->
                            // Retrieve details for each place
                            val placeRequest = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()

                            placesClient.fetchPlace(placeRequest)
                                .addOnCompleteListener { innerTask ->
                                    if (innerTask.isSuccessful) {
                                        val place = innerTask.result?.place
                                        // Access place details (e.g., place.name, place.latLng)
                                        if (place != null) {
                                            val name = place.name
                                            val latLng = place.latLng
                                            destinationLocation = latLng
                                            // Handle the retrieved information as needed
                                            Log.d("PlaceInfo", "Name: $name, LatLng: $latLng")
                                            val marker = mMap.addMarker(
                                                MarkerOptions()
                                                    .position(latLng)
                                                    .title(place.name)
                                                    .snippet(place.address)
                                            )



                                        }
                                    } else {
                                        // Handle innerTask exception
                                        val exception = innerTask.exception
                                        exception?.printStackTrace()
                                    }
                                }
                            mMap.setOnMarkerClickListener { marker ->
                                // Handle marker click here
                                // Retrieve the LatLng from the marker's tag

                                // Call traceRoute with sourceLocation and destinationLatLng
                                traceRoute(sourceLocation!!,  marker.position!!)
                                true
                            }
                        }
                    } else {
                        // Handle task exception
                        val exception = task.exception
                        exception?.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }



    }
    @SuppressLint("PotentialBehaviorOverride")
    private fun showDoctorsInCountry() {
        mMap.clear()
        // Specify the fields to return (in this case, just the place name and location)
        val placeFields: List<Field> = listOf(Field.NAME, Field.LAT_LNG)

        // Get the country and region information using Geocoding API
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses: List<Address>?
        try {
            // Replace "YOUR_LATITUDE" and "YOUR_LONGITUDE" with the actual latitude and longitude
            addresses = geocoder.getFromLocation(sourceLocation!!.latitude, sourceLocation!!.longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val country = addresses[0].countryName
                val region = addresses[0].adminArea

                // Create a FindAutocompletePredictionsRequest with type filter for hospitals
                val request = FindAutocompletePredictionsRequest.builder()
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setSessionToken(AutocompleteSessionToken.newInstance())
                    .setQuery("doctor $region") // Search query for hospitals with country and region
                    .build()

                // Perform the Autocomplete request
                val autocompleteResponse = placesClient.findAutocompletePredictions(request)

                autocompleteResponse.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val response = task.result

                        // Process the Autocomplete response
                        response?.autocompletePredictions?.forEach { prediction ->
                            // Retrieve details for each place
                            val placeRequest = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()

                            placesClient.fetchPlace(placeRequest)
                                .addOnCompleteListener { innerTask ->
                                    if (innerTask.isSuccessful) {
                                        val place = innerTask.result?.place
                                        // Access place details (e.g., place.name, place.latLng)
                                        if (place != null) {
                                            val name = place.name
                                            val latLng = place.latLng
                                            destinationLocation = latLng
                                            // Handle the retrieved information as needed
                                            Log.d("PlaceInfo", "Name: $name, LatLng: $latLng")
                                            val marker = mMap.addMarker(
                                                MarkerOptions()
                                                    .position(latLng)
                                                    .title(place.name)
                                                    .snippet(place.address)
                                            )



                                        }
                                    } else {
                                        // Handle innerTask exception
                                        val exception = innerTask.exception
                                        exception?.printStackTrace()
                                    }
                                }
                            mMap.setOnMarkerClickListener { marker ->
                                // Handle marker click here
                                // Retrieve the LatLng from the marker's tag

                                // Call traceRoute with sourceLocation and destinationLatLng
                                traceRoute(sourceLocation!!,  marker.position!!)
                                true
                            }
                        }
                    } else {
                        // Handle task exception
                        val exception = task.exception
                        exception?.printStackTrace()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }



    }


    private fun moveCameraToShowAllHospitals(hospitalLatLngList: List<LatLng>) {
        // Check if the list is not empty
        if (hospitalLatLngList.isNotEmpty()) {
            // Create a LatLngBounds.Builder to include all hospitals
            val builder = LatLngBounds.builder()
            for (latLng in hospitalLatLngList) {
                builder.include(latLng)
            }

            // Build the bounds
            val bounds = builder.build()

            // Move the camera to display all hospitals
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, CAMERA_PADDING))
        } else {
            // Log a message or handle the case when no hospitals are found
            Log.d("MyApp", "No hospitals found.")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        /*val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/

        // Now that mMap is initialized, you can use it in handleMyLocationClick
        val searchEditText = binding.searchEditText
        val sourceBTN = binding.btnPosition
        sourceBTN.setOnClickListener {
            handleMyLocationClick(requireContext(), searchEditText)
        }

    }
    private fun fetchDriverData() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                //val driversResponse = driverServices.getDrivers().execute()
                //val carsResponse = driverServices.getCars().execute()
                //val usersResponse = driverServices.getUsers().execute()
                //val geocoder = Geocoder(requireContext(), Locale.getDefault())
                //val DriversBtn = binding.btnRide
                //val addresses: List<Address>?
                withContext(Dispatchers.Main) {
                    driverAdapter.setData(staticDrivers, staticUsers, staticCars, "YourLocation")
                   Log.d("data","$driverAdapter,$staticDrivers")

                        EmgActivity.updateRecyclerView(driverAdapter)


                  /*  if (driversResponse.isSuccessful && carsResponse.isSuccessful && usersResponse.isSuccessful) {
                        val drivers = driversResponse.body() ?: emptyList()
                        val cars = carsResponse.body() ?: emptyList()
                        val users = usersResponse.body() ?: emptyList()
                        addresses = geocoder.getFromLocation(sourceLocation!!.latitude, sourceLocation!!.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val region = addresses[0].adminArea
                            driverAdapter.setData(staticDrivers, staticUsers, staticCars, "YourLocation")
                        }


                            //DriversBtn.isEnabled = true
                    } else {
                        // Handle unsuccessful responses
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
                    }*/
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle exceptions
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("Error : ","${e.message}")
                }
            }
        }
    }


    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:9090/") // Replace with your actual backend URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



    override fun onMyLocationButtonClick(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMyLocationClick(p0: Location) {
        TODO("Not yet implemented")
    }
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val CAMERA_PADDING = 100
    }

}