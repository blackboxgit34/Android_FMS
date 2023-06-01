package com.humbhi.blackbox.ui.data.models

data class ConsolidateVoilationModel(
    val aaData: List<ConsolidateVoilationData>,
    val iTotalDisplayRecords: Int,
    val iTotalRecords: Int,
    val sEcho: Int
)