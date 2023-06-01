package com.humbhi.blackbox.ui.ui.notification

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.humbhi.blackbox.ui.data.DataManager
import com.humbhi.blackbox.ui.data.models.NotificationResponseModel
import com.humbhi.blackbox.ui.data.network.ApiError
import com.humbhi.blackbox.ui.data.network.api.ApiHelper

class NotificationPresenterImpl(
    private val mNotificationView: NotificationView,
private val mDataManager: DataManager
) : NotificationPresenter{
    override fun getAllNotificationListAPI(custid: String) {
        when{
            mNotificationView.isNetworkConnected()->{
                mNotificationView.isShowLoading()
                mDataManager.apiCallToGetNotification(custid,object : ApiHelper.ApiListener{
                    override fun onSuccess(commonResponse: Any) {
                        mNotificationView.isHideLoading()


                        val getAllNotifications = Gson().fromJson(
                            Gson().toJson(commonResponse), NotificationResponseModel::class.java
                        )

                        //mNotificationView.getNotificationListData(homedateList)
                    }

                    override fun onError(errorId: Any) {
                        mNotificationView.isHideLoading()
                        mNotificationView.showErrorMessage("Network Issue, Please try after sometime")
                    }

                    override fun onFailure(apiError: ApiError?, throwable: Throwable?) {
                        mNotificationView.isHideLoading()
                        mNotificationView.showErrorMessage("Something went wrong. Please connect BlackBox team.")
                    }

                })
            }
        }
    }

}