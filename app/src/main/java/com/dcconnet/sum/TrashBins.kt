package com.dcconnet.sum

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dcconnet.sum.databinding.ActivityTrashBinsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TrashBins : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTrashBinsBinding
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var addDialog: AddNewRecyclerDialog

    private val database = FirebaseDatabase.getInstance().reference
    private var databaseBook: DatabaseReference? = database.child("mapbin").child("Bölge1")

    private var listOfRecyclers = listOf<RecyclerBin?>()

    private val adapter: TrashBinAdapter by lazy { TrashBinAdapter() }
    private var isMapReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrashBinsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        previewDialog()
        setAdapter()
        setMapView(savedInstanceState)
        getRecyclerBin()
    }

    private fun setMapView(savedInstanceState: Bundle?) {
        mapView = binding.trashBinMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    private fun setAdapter() {
        binding.trashBinRecyclerView.adapter = adapter
        adapter.onButtonClickListener = {
            updateRecyclerStatus(it)
        }
    }

    private fun updateRecyclerStatus(item: RecyclerBin) {
        databaseBook?.get()?.addOnSuccessListener { snapshot ->
            snapshot?.children
                ?.map { it.getValue(RecyclerBin::class.java) }
                ?.firstOrNull {
                    it?.lat == item.lat
                }?.let { recyclerBin ->
                    databaseBook
                        ?.child(recyclerBin.currentTime ?: "")
                        ?.child("binStatus")
                        ?.setValue(recyclerBin.binStatus.setOpposite())
                }
        }
    }

    private fun previewDialog() {
        addDialog = AddNewRecyclerDialog(this)
        addDialog.onButtonClickListener = { chipGroup, latlon, title ->
            var recyclerBin =
                RecyclerBin(
                    lat = latlon.latitude,
                    lon = latlon.longitude,
                    title = title,
                    currentTime = System.currentTimeMillis().toString()
                )
            chipGroup.forEach { chip ->
                when (chip.text) {
                    "Kağıt" -> {
                        recyclerBin = recyclerBin.copy(typePaper = true)
                    }

                    "Plastik" -> {
                        recyclerBin = recyclerBin.copy(typePlastic = true)
                    }

                    "Cam" -> {
                        recyclerBin = recyclerBin.copy(typeGlass = true)
                    }
                }
            }
            updateMarker(recyclerBin)
        }
    }

    private fun getRecyclerBin() {
        val dataListener1 = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val recyclerList =
                        dataSnapshot.children.map { it.getValue(RecyclerBin::class.java) }
                    if (!recyclerList.isNullOrEmpty()) {
                        adapter.submitList(recyclerList)
                        if (isMapReady) {
                            recyclerList.forEach {
                                addMarkers(it)
                            }
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

    private fun updateMarker(recyclerBin: RecyclerBin) {
        databaseBook?.child(recyclerBin.currentTime ?: "")?.setValue(recyclerBin)
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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        isMapReady = true
        if (!listOfRecyclers.isNullOrEmpty()) {
            listOfRecyclers.forEach {
                addMarkers(it)
            }
        }
        googleMap.setOnMapLongClickListener {
            showDialog(it)
        }
    }

    private fun addMarkers(item: RecyclerBin?) {
        val location = LatLng(item?.lat ?: 0.0, item?.lon ?: 0.0)
        googleMap.addMarker(MarkerOptions().position(location).title(item?.title))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

    private fun showDialog(latLon: LatLng) {
        addDialog.setLatLon(latLon)
        addDialog.show()
    }

}

private fun String?.setOpposite(): String {
    return if (this == "Boş") {
        "Dolu"
    } else {
        "Boş"
    }
}
