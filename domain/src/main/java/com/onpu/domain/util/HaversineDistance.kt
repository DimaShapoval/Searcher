package com.onpu.domain.util

import kotlin.math.*

/*
 This is the implementation Haversine Distance Algorithm between two places
 @author ananth
 R = earth’s radius (mean radius = 6,371km)
 Δlat = lat2− lat1
 Δlong = long2− long1
 a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
 c = 2.atan2(√a, √(1−a))
 d = R.c
*/

fun distanceInKmBetweenTwoPlaces(place1: PlaceCoordinates, place2: PlaceCoordinates): Double {
    val latDistance = Math.toRadians(place1.lat - place2.lat)
    val lonDistance = Math.toRadians(place1.lon - place2.lon)

    val a = sin(latDistance / 2).pow(2.0) +
            cos(Math.toRadians(place1.lat)) * cos(Math.toRadians(place2.lat)) *
            sin(lonDistance/2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val earthRadius = 6371
    val distance = earthRadius * c
    return distance.round()
}

data class PlaceCoordinates(
    val lat: Double = 0.0,
    val lon: Double = 0.0
)

private fun Double.round() = this.times(10f).roundToInt().toDouble().div(10f)