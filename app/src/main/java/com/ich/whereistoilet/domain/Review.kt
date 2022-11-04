package com.ich.whereistoilet.domain

data class Review(
    val id: String = "",
    val userId: String = "",
    val toiletId: Int = 0,
    val stars: Int = 0,
    val cleanRate: Int = 0,
    val content: String = "",
    val date: String = ""
)