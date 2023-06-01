package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class ComplaintTypeModel implements Serializable {

    String Description,ID;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
