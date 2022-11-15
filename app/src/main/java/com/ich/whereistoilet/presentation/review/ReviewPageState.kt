package com.ich.whereistoilet.presentation.review

import com.ich.whereistoilet.domain.model.Review
import com.ich.whereistoilet.domain.model.ToiletInfo

data class ReviewPageState(
    val toilets: List<ToiletInfo> = emptyList(),
    val isLoading: Boolean = false,
    val selectedToilet: ToiletInfo? = null,
    val errorMsg: String = "",
    val reviewList: List<Review> = emptyList()
)
