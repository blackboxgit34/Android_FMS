package com.humbhi.blackbox.ui.data.models

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize


data class LoginResponse(
    var AdminPassword: Any,
    var Announcement: String,
    var AppVersion: String,
    var ContactStatus: Any,
    var CustId: String,
    var CustKey: String,
    var CustName: String,
    var CustType: String,
    var CustTypeID: Int,
    var IsSubUser: Boolean,
    var Mobile: Any,
    var Password: Any,
    var ResponseStatus: LoginResponseStatus,
    var TrialPeriodWithFuel: Any,
    var UserName: String,
    var UserType: Any,
    var bannerImage: String,
    var maps: String,
    var panic: Any,
    var paymentForAppLink: Any,
    var showIcons: String,
    var tokenUpdated: Any,
    var versioncode: String
)
