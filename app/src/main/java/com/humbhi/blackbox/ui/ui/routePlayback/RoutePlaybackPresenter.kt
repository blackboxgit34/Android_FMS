package com.humbhi.blackbox.ui.ui.routePlayback

interface RoutePlaybackPresenter {
    fun hitRoutePlaybackApi(
        tableName: String,
        fromDate: String,
        toDate: String
    )

    fun hitDrivingBehaviourRouteAPI(  tableName: String,
                                      fromDate: String,
                                      toDate: String)
}