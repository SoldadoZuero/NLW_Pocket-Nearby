package com.rocketseat.nlw.nearby.ui.component.map

import org.osmdroid.util.GeoPoint

// Encontra o ponto sudoeste (menor latitude e menor longitude)
fun calculateCenter(points: List<GeoPoint>): GeoPoint {
    val latitudes = points.map { it.latitude }
    val longitudes = points.map { it.longitude }

    val avgLat = latitudes.sum() / latitudes.size
    val avgLon = longitudes.sum() / longitudes.size

    return GeoPoint(avgLat, avgLon)
}
