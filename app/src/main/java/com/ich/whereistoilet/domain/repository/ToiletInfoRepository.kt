package com.ich.whereistoilet.domain.repository

import com.ich.whereistoilet.common.util.Resource
import com.ich.whereistoilet.domain.model.ToiletInfo

interface ToiletInfoRepository {
    suspend fun getToiletInfo(): Resource<List<ToiletInfo>>
}