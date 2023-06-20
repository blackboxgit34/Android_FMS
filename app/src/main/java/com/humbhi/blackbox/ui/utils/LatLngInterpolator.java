package com.humbhi.blackbox.ui.utils;

import com.google.android.gms.maps.model.LatLng;

public interface LatLngInterpolator {
    LatLng interpolate(float fraction, LatLng startValue, LatLng endValue);

    class Linear implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng startValue, LatLng endValue) {
            double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
            double lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude;
            return new LatLng(lat, lng);
        }
    }

    class LinearFixed implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng startValue, LatLng endValue) {
            double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
            double lngDelta = endValue.longitude - startValue.longitude;

            // Take the shortest path around the globe
            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }

            double lng = lngDelta * fraction + startValue.longitude;
            return new LatLng(lat, lng);
        }
    }
}

