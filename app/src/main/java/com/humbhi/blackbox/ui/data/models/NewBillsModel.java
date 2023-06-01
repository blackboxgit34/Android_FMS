package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class NewBillsModel implements Serializable {

    String custid,billno,BoxCount,billperiod,currentBalance,OtherCharges,Discount,PreviousBalance,totalBalance,FromDate,type;

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public String getBoxCount() {
        return BoxCount;
    }

    public void setBoxCount(String boxCount) {
        BoxCount = boxCount;
    }

    public String getBillperiod() {
        return billperiod;
    }

    public void setBillperiod(String billperiod) {
        this.billperiod = billperiod;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getOtherCharges() {
        return OtherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        OtherCharges = otherCharges;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPreviousBalance() {
        return PreviousBalance;
    }

    public void setPreviousBalance(String previousBalance) {
        PreviousBalance = previousBalance;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
