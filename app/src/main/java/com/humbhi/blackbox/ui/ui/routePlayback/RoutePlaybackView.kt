package com.humbhi.blackbox.ui.ui.routePlayback

import com.humbhi.blackbox.ui.data.models.DrivingBehaviourRouteDataModel
import com.humbhi.blackbox.ui.data.models.RoutePlaybackResponseModel

interface RoutePlaybackView {

    fun getRoutePlaybackResponse(routePlaybackResponseModel: RoutePlaybackResponseModel)

    fun getDrivingBehavourRouteData(drivingBehaviourRouteDataModel: DrivingBehaviourRouteDataModel)

    fun isNetworkConnected(): Boolean

    fun isShowLoading(): Boolean

    fun isHideLoading(): Boolean

    fun showErrorMessage(string: String)
}