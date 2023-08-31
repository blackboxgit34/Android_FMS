package com.humbhi.blackbox.ui.data.models

data class ViewFenceModel(
    val aaData: List<FenceData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)