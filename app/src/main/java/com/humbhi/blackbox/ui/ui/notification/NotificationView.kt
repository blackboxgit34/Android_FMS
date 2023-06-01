package com.humbhi.blackbox.ui.ui.notification

import com.humbhi.blackbox.ui.data.models.NotificationResponseModel


interface NotificationView {

    fun getNotificationListData(notificationDataList: NotificationResponseModel)

    fun isNetworkConnected():Boolean

    fun isShowLoading():Boolean

    fun isHideLoading():Boolean

    fun showErrorMessage(string: String)
}