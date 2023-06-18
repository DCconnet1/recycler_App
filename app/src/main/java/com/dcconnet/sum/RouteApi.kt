package com.dcconnet.sum

import com.dcconnet.sum.data.RouteResult
import com.google.maps.model.DirectionsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RouteApi {
    @GET("maps/api/directions/json?mode=driving&transit_routing_preference=less_driving")
    suspend fun getBestRoute(
        @Query("origin") currentLocation: String,
        @Query("destination") destinationLocation: String,
        @Query("key") key: String = "AIzaSyAB93QC17m-lfyB5KaYQVZWirGSSdcg6Og"
    ): RouteResult
}