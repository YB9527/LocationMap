package com.xupu.locationmap.common.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import java.util.List;

public class MyLocationUtil {


    private static  List<String> list;
    private static LocationManager locationManager;
    static {
         locationManager = (LocationManager) AndroidTool.getMainActivity().getSystemService(Context.LOCATION_SERVICE);
         list = locationManager.getProviders(true);
        if (!list.contains(locationManager.GPS_PROVIDER)) {
            list.add(locationManager.GPS_PROVIDER);
        }
        if (!list.contains(locationManager.GPS_PROVIDER)) {
            list.add(locationManager.GPS_PROVIDER);
        }
        if (!list.contains(locationManager.NETWORK_PROVIDER)) {
            list.add(locationManager.NETWORK_PROVIDER);
        }
    }

    @SuppressLint("MissingPermission")
    public static Location getMyLocation() {

        Location lastKnownLocation = null;

        lastKnownLocation = locationManager.getLastKnownLocation(list.get(1));
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(list.get(0));
        }
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(list.get(2));
        }


        return lastKnownLocation;
    }
}
