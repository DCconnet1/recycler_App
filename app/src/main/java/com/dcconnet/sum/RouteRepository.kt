package com.dcconnet.sum

import android.util.Log
import com.dcconnet.sum.data.RouteResult
import com.google.maps.model.DirectionsResult

class RouteRepository {
    suspend fun getRoute(
        currentLocation: String,
        destinationLocation: String,
        onResponse: ((RouteResult) -> Unit)?
    ) {
        try {
            val bestRoute = Client.api.getBestRoute(
                currentLocation = currentLocation,
                destinationLocation = destinationLocation
            )

            onResponse?.invoke(bestRoute)
        } catch (e: Exception) {
            Log.d("RouteRepository", "getRoute: $e")
        }

    }
}