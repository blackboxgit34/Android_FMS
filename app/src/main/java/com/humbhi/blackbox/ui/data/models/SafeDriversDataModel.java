package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class SafeDriversDataModel implements Serializable {

    String iTotalRecords;
    String sno,VehName,Driver,HA,HB,RT,OS,total;

    public String getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(String iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getVehName() {
        return VehName;
    }

    public void setVehName(String vehName) {
        VehName = vehName;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getHA() {
        return HA;
    }

    public void setHA(String HA) {
        this.HA = HA;
    }

    public String getHB() {
        return HB;
    }

    public void setHB(String HB) {
        this.HB = HB;
    }

    public String getRT() {
        return RT;
    }

    public void setRT(String RT) {
        this.RT = RT;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
