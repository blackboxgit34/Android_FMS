package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class FuelGraphModel implements Serializable {
    String Speed,Distance,Location,FuelLevel,DateTime;

    public FuelGraphModel(String speed, String distance, String location, String fuelLevel,String dateTime) {
        Speed = speed;
        Distance = distance;
        Location = location;
        FuelLevel = fuelLevel;
        DateTime = dateTime;
    }

    public String getSpeed() {
        return Speed;
    }

    public String getDistance() {
        return Distance;
    }

    public String getLocation() {
        return Location;
    }

    public String getFuelLevel() {
        return FuelLevel;
    }

    public String getDateTime() {
        return DateTime;
    }
}
