package com.ich.whereistoilet.domain.model

data class Review(
    val userId: String = "",
    val toiletId: String = "",
    val stars: Float = 0f,
    val cleanRate: Float = 0f,
    val content: String = "",
    val date: Long = 0L,
    val thumbUp: Int = 0,
    val thumbUpUsers: Map<String, Boolean> = emptyMap()
){
    fun getReviewString(): String{
        val regex = Regex("[^A-Za-z0-9]")
        val userIdFiltered = regex.replace(userId, "")
        return "${userIdFiltered}${toiletId}${date}"
    }
}