package com.humbhi.blackbox.ui.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationAddress {

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final GeocoderHandler handler) {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0) {
                        Address address = addresses.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String address) {
                if (address == null) {
                    address = "Unable to get address for this location.";
                }
                handler.onAddressFetched(address);
            }
        };
        task.execute();
    }

    public interface GeocoderHandler {
        void onAddressFetched(String address);
    }
}

