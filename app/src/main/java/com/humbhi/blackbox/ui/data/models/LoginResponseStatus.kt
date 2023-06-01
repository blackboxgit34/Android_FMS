package com.humbhi.blackbox.ui.data.models

data class LoginResponseStatus(
    val Error: Any,
    val ErrorCode: Any,
    val Message: Any,
    val StackTrace: Any,
    val Status: Boolean
)