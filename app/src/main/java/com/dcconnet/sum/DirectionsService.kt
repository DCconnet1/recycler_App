package com.dcconnet.sum

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface DirectionsService {
    @GET("/maps/api/directions/json")
    fun getDirections(
        @Query("mode") mode: String?,
        @Query("origin") origin: String?,
        @Query("destination") destination: String?,
        @Query("key") apiKey: String?
    ): Call<DirectionResponse>
}



