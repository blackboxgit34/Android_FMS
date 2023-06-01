package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class AllVehicleModel implements Serializable {

    String vehname,bbid;

    public String getVehname() {
        return vehname;
    }

    public void setVehname(String vehname) {
        this.vehname = vehname;
    }

    public String getBbid() {
        return bbid;
    }

    public void setBbid(String bbid) {
        this.bbid = bbid;
    }
}
