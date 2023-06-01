package com.humbhi.blackbox.ui.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class LiveTrackingVehicleDataResponse(
    val Latitude:Double,
    val Longitude:Double,
    val LocationWithoutLink:String
):ClusterItem{
    private val position: LatLng
    private val title: String
    private val snippet: String
    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return title
    }

    override fun getSnippet(): String? {
        return snippet
    }

    init {
        position = LatLng(Latitude, Longitude)
        this.title = LocationWithoutLink
        this.snippet = LocationWithoutLink
    }

}
