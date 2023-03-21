package com.dcconnet.sum

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationChangeViewModel: ViewModel() {

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation : LiveData<Location> get() = _currentLocation


    fun setCurrentLocation(location: Location){
        _currentLocation.postValue(location)
    }


}