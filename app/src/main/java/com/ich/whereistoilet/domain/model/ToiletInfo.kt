package com.ich.whereistoilet.domain.model

data class ToiletInfo(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val insertDate: String = "",
    val updateDate: String = ""
)