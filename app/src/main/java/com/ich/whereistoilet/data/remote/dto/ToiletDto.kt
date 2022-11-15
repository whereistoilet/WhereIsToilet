package com.ich.whereistoilet.data.remote.dto

import com.ich.whereistoilet.domain.model.ToiletInfo

data class ToiletDto(
    val POI_ID: String = "",
    val FNAME: String = "",
    val ANAME: String = "",
    val CNAME: String = "",
    val CENTER_X1: Double = 0.0,
    val CENTER_Y1: Double = 0.0,
    val X_WGS84: Double = 0.0,
    val Y_WGS84: Double = 0.0,
    val INSERTDATE: String = "",
    val UPDATEDATE: String = ""
)

fun ToiletDto.toToiletInfo(): ToiletInfo {
    return ToiletInfo(
        id = POI_ID,
        name = FNAME,
        type = ANAME,
        lat = Y_WGS84,
        lng = X_WGS84,
        insertDate = INSERTDATE,
        updateDate = UPDATEDATE
    )
}