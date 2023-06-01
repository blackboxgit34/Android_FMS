package com.humbhi.blackbox.ui.data.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {

    String typeId,NotificationMsg,NotificationRead,NotificationDate,notificationId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getNotificationMsg() {
        return NotificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        NotificationMsg = notificationMsg;
    }

    public String getNotificationRead() {
        return NotificationRead;
    }

    public void setNotificationRead(String notificationRead) {
        NotificationRead = notificationRead;
    }

    public String getNotificationDate() {
        return NotificationDate;
    }

    public void setNotificationDate(String notificationDate)
    {
        NotificationDate = notificationDate;
    }

    public String getNotificationId()
    {
        return notificationId;
    }

    public void setNotificationId(String notificationId)
    {
        this.notificationId = notificationId;
    }
}
