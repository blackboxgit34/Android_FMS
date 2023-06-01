package com.humbhi.blackbox.ui.utils;

import com.google.android.gms.maps.model.LatLng;

public class ColouredPoint {


        public LatLng coords;
        public int color;

        public ColouredPoint(LatLng coords, int color) {
            this.coords = coords;
            this.color = color;
        }

}
