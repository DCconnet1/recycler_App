package com.dcconnet.sum

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dcconnet.sum.data.RouteResult
import com.dcconnet.sum.databinding.ActivityMapdenemeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale


class Mapdeneme : AppCompatActivity(), OnMapReadyCallback {

    private val repository: Repoloc by lazy { Repoloc() }
    private lateinit var binding: ActivityMapdenemeBinding
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var viewModel: LocationChangeViewModel

    private lateinit var mapView: MapView

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("mapbin").child("Bölge1")

    private var listOfRecyclers = listOf<RecyclerBin?>()
    private var isMapReady = false
    private var isRouteStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LocationChangeViewModel()
        binding = ActivityMapdenemeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setRoute()

        setMapView(savedInstanceState)
        binding.button1.setOnClickListener {
            getRecyclerBin()
        }

        setObservers()
    }

    private fun setMapView(savedInstanceState: Bundle?) {
        mapView = binding.map
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }


    private fun setObservers() {
        viewModel.bestRoute.observe(this) {
            drawRoute(it)
        }
    }

    private fun drawRoute(route: RouteResult?) {
        route?.routes?.get(0)?.let {
            val points =  route.routes.get(0)?.overview_polyline?.points
            val latLong = PolylineUtils.decodePolyLines(points)
            val polylineOptions = PolylineOptions()
            polylineOptions.addAll(latLong)
            polylineOptions.width(12f)
            polylineOptions.color(Color.BLUE)
            mMap.addPolyline(polylineOptions)

        }

    }

    private fun getRecyclerBin() {
        val dataListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recyclerList =
                        dataSnapshot.children.map { it.getValue(RecyclerBin::class.java) }
                    if (!recyclerList.isNullOrEmpty()) {
                        if (isMapReady) {
                            val destination = recyclerList.get(0)
                            val location = viewModel.currentLocation.value
                            startRoute(
                                LatLng(
                                    location?.latitude ?: 0.0,
                                    location?.longitude ?: 0.0
                                ), LatLng(destination?.lat ?: 0.0, destination?.lon ?: 0.0)
                            )
                            isRouteStarted = true
                        } else {
                            listOfRecyclers = recyclerList
                        }

                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Veri çekme hatası: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        databaseBook?.addValueEventListener(dataListener1)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        isMapReady = true
        mMap = googleMap
        mMap.setOnMapLongClickListener(listener)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { p0 ->
            mMap.clear()
            viewModel.setCurrentLocation(p0)
            val currentLoc = LatLng(p0.latitude, p0.longitude)
            mMap.addMarker(
                MarkerOptions().position(currentLoc)
                    .title(currentLoc.latitude.toString() + " " + currentLoc.longitude.toString())
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15f))
        }
        checkIfUserHasPermission()
        if (!isRouteStarted && !listOfRecyclers.isNullOrEmpty()) {
            val destination = listOfRecyclers.get(0)

            val location = viewModel.currentLocation.value
            startRoute(
                LatLng(
                    location?.latitude ?: 0.0,
                    location?.longitude ?: 0.0
                ), LatLng(destination?.lat ?: 0.0, destination?.lon ?: 0.0)
            )
        }

    }

    private fun checkIfUserHasPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1f,
                locationListener
            )
            val lastloc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastloc != null) {
                val lastlocLatLng = LatLng(lastloc.latitude, lastloc.longitude)
                mMap.addMarker(
                    MarkerOptions().position(lastlocLatLng).title("En son bulunduğunuz konum")
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastlocLatLng, 5f))
            }

        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == 1
            && grantResults.isNotEmpty()
            && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1f,
                locationListener
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val listener = GoogleMap.OnMapLongClickListener { p0 ->
        mMap.clear()
        val geocoder = Geocoder(this@Mapdeneme, Locale.getDefault())
        var adres = ""
        try {
            val adresListesi = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
            if (adresListesi!!.size > 0) {
                if (adresListesi.get(0).thoroughfare != null) {
                    adres += adresListesi.get(0).thoroughfare
                    adres += " "

                    if (adresListesi.get(0).subThoroughfare != null) {
                        adres += adresListesi.get(0).subThoroughfare
                        adres += " "

                        if (adresListesi.get(0).countryName != null) {
                            adres += adresListesi.get(0).countryName
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMap.addMarker(MarkerOptions().position(p0).title(adres))
    }

    private fun startRoute(current: LatLng, destination: LatLng) {
        val currentLocation = current.latitude.toString() + "," + current.longitude.toString()
        val destinationLocation =
            destination.latitude.toString() + "," + destination.longitude.toString()
        viewModel.getBestRoute(
            currentLocation = currentLocation,
            destinationLocation = destinationLocation
        )
    }

    private fun convertLatLng(latLng: com.google.maps.model.LatLng): LatLng {
        return LatLng(latLng.lat, latLng.lng)
    }

    private fun setRoute() {

        binding.button1.setOnClickListener {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val directionsService = retrofit.create(DirectionsService::class.java)

            val origin = LatLng(41.0082, 28.9784)  // Başlangıç konumu
            val destination = LatLng(39.9334, 32.8597)  // Hedef konumu

            val request = directionsService.getDirections(
                "driving",
                "${origin.latitude},${origin.longitude}",
                "${destination.latitude},${destination.longitude}",
                "AIzaSyAB93QC17m-lfyB5KaYQVZWirGSSdcg6Og"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}



