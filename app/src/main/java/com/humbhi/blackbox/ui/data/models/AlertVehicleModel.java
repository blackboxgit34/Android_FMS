package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class AlertVehicleModel implements Serializable {

    String BoxID,VehicleName,GeofenceAlert,BatteryDisconnection,OverSpeedAlert,IgnitionStatusAlert,FMSVehicleId,messageType,OverSpeedLimit,overStoppageAlert,OverStopLimit;

    public String getBoxID() {
        return BoxID;
    }

    public void setBoxID(String boxID) {
        BoxID = boxID;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getGeofenceAlert() {
        return GeofenceAlert;
    }

    public void setGeofenceAlert(String geofenceAlert) {
        GeofenceAlert = geofenceAlert;
    }

    public String getBatteryDisconnection() {
        return BatteryDisconnection;
    }

    public void setBatteryDisconnection(String batteryDisconnection) {
        BatteryDisconnection = batteryDisconnection;
    }

    public String getOverSpeedAlert() {
        return OverSpeedAlert;
    }

    public void setOverSpeedAlert(String overSpeedAlert) {
        OverSpeedAlert = overSpeedAlert;
    }

    public String getIgnitionStatusAlert() {
        return IgnitionStatusAlert;
    }

    public void setIgnitionStatusAlert(String ignitionStatusAlert) {
        IgnitionStatusAlert = ignitionStatusAlert;
    }

    public String getFMSVehicleId() {
        return FMSVehicleId;
    }

    public void setFMSVehicleId(String FMSVehicleId) {
        this.FMSVehicleId = FMSVehicleId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getOverSpeedLimit() {
        return OverSpeedLimit;
    }

    public void setOverSpeedLimit(String overSpeedLimit) {
        OverSpeedLimit = overSpeedLimit;
    }

    public String getOverStoppageAlert() {
        return overStoppageAlert;
    }

    public void setOverStoppageAlert(String overStoppageAlert) {
        this.overStoppageAlert = overStoppageAlert;
    }

    public String getOverStopLimit() {
        return OverStopLimit;
    }

    public void setOverStopLimit(String overStopLimit) {
        OverStopLimit = overStopLimit;
    }
}
