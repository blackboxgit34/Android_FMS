package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class AllDriverModel implements Serializable{

    String Text,Value;

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
