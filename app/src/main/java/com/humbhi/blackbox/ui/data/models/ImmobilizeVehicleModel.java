package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class ImmobilizeVehicleModel implements Serializable {

    String vehicleName, bbid,location,IsImmobilizeActive,immobilize,lastdate,immobilisze;

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getBbid() {
        return bbid;
    }

    public void setBbid(String bbid) {
        this.bbid = bbid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIsImmobilizeActive() {
        return IsImmobilizeActive;
    }

    public void setIsImmobilizeActive(String isImmobilizeActive) {
        IsImmobilizeActive = isImmobilizeActive;
    }

    public String getImmobilize() {
        return immobilize;
    }

    public void setImmobilize(String immobilize) {
        this.immobilize = immobilize;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }

    public String getImmobilisze() {
        return immobilisze;
    }

    public void setImmobilisze(String immobilisze) {
        this.immobilisze = immobilisze;
    }
}
