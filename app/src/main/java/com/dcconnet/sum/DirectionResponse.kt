package com.dcconnet.sum


data class DirectionResponse(
    val routes: ArrayList<Route>
)

data class Route(
    val overview_polyline: OverviewPolyline
)

data class OverviewPolyline(
    val points: String
)


