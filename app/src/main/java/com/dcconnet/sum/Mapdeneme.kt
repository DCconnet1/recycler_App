package com.dcconnet.sum

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dcconnet.sum.databinding.ActivityMapdenemeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.lang.Exception
import java.net.URL
import java.util.*

class Mapdeneme : AppCompatActivity(), OnMapReadyCallback {

    private val repository: Repoloc by lazy { Repoloc() }
    private lateinit var binding: ActivityMapdenemeBinding
    private lateinit var mMap: GoogleMap
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var viewModel: LocationChangeViewModel






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = LocationChangeViewModel()
        binding = ActivityMapdenemeBinding.inflate(layoutInflater)
        val view =binding.root
        setContentView(view)
        setButtonListeners()
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(listener)




        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(p0: Location) {
                mMap.clear()
                viewModel.setCurrentLocation(p0)
                val currentLoc = LatLng(p0.latitude,p0.longitude)
                mMap.addMarker(MarkerOptions().position(currentLoc).title(currentLoc.latitude.toString()+" "+ currentLoc.longitude.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,15f))
                val geocoder = Geocoder(this@Mapdeneme, Locale.getDefault())



                try {
                    val adresListesi = geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if (adresListesi!!.size > 0 ){
                        println(adresListesi.get(0).toString())
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }

        }



        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val lastloc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastloc != null){
                val lastlocLatLng = LatLng(lastloc.latitude,lastloc.longitude)
                mMap.addMarker(MarkerOptions().position(lastlocLatLng).title("En son bulunduğunuz konum"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastlocLatLng,15f))
            }

        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.size > 0){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    val listener = object : GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng) {
            mMap.clear()
            val geocoder = Geocoder(this@Mapdeneme, Locale.getDefault())
            if(p0 !=null){
                var adres = ""
                try {
                    val adresListesi = geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if(adresListesi!!.size >0){
                       if(adresListesi.get(0).thoroughfare != null){
                           adres += adresListesi.get(0).thoroughfare
                           adres+= " "

                           if(adresListesi.get(0).subThoroughfare != null){
                               adres += adresListesi.get(0).subThoroughfare
                               adres+= " "

                               if(adresListesi.get(0).countryName != null){
                                   adres += adresListesi.get(0).countryName
                               }
                           }
                       }
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
                mMap.addMarker(MarkerOptions().position(p0).title(adres))
            }


        }



    }

    fun setButtonListeners() {
        binding.button.setOnClickListener {
            val myCurrentLocation = viewModel.currentLocation.value
            myCurrentLocation?.let { location -> checkIfUserExist(loca= location) }

        }
    }


    private fun loginUser(loca: Location) {
        repository.loginUser(loca = Location()) { isLogin, isError ->
            if (!isError){
                if (isLogin) {
                    Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()

                }else {
                    Toast.makeText(this, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT)
                        .show()
                }
            }else {
                Toast.makeText(this, "Bir hata meydana geldi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkIfUserExist(loca: Location) {
        repository.setCurrentLocation(loca = loca)



}
    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }
}






