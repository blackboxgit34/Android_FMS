package com.humbhi.blackbox.ui.Utility

import android.animation.TypeEvaluator
import com.google.android.gms.maps.model.LatLng

class LatLngEvaluator : TypeEvaluator<LatLng> {
    override fun evaluate(fraction: Float, startValue: LatLng, endValue: LatLng): LatLng {
        val latitude = startValue.latitude + (endValue.latitude - startValue.latitude) * fraction
        val longitude = startValue.longitude + (endValue.longitude - startValue.longitude) * fraction
        return LatLng(latitude, longitude)
    }
}
