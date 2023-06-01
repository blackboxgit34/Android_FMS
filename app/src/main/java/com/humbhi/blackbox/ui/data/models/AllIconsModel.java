package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class AllIconsModel implements Serializable {
    String ID,Text;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }
}
