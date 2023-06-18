package com.dcconnet.sum

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcconnet.sum.data.RouteResult
import com.google.maps.model.DirectionsResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationChangeViewModel: ViewModel() {

    private lateinit var repository: RouteRepository

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation : LiveData<Location> get() = _currentLocation

    private val _bestRoute: MutableLiveData<RouteResult> = MutableLiveData()
    val bestRoute : LiveData<RouteResult> get() = _bestRoute


    fun setCurrentLocation(location: Location){
        _currentLocation.postValue(location)
    }
    init {
        repository = RouteRepository()
    }

    fun getBestRoute(
        currentLocation: String,
        destinationLocation: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getRoute(
                currentLocation = currentLocation,
                destinationLocation = destinationLocation
            ){
                _bestRoute.postValue(it)
            }
        }
    }
}