package com.ich.whereistoilet.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun timeMillisToDate(millis: Long): String{
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = Date(millis)
        return format.format(date)
    }
}