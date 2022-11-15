package com.ich.whereistoilet.data.repository

import com.ich.whereistoilet.common.util.Resource
import com.ich.whereistoilet.data.remote.dto.ToiletDto
import com.ich.whereistoilet.data.remote.dto.toToiletInfo
import com.ich.whereistoilet.domain.model.ToiletInfo
import com.ich.whereistoilet.domain.repository.ToiletInfoRepository
import retrofit2.HttpException
import java.io.IOException

class ToiletInfoRepositoryImpl(
    // api: ToiletInfoApi
) : ToiletInfoRepository {
    override suspend fun getToiletInfo(): Resource<List<ToiletInfo>> {
        val data = try{
            testData.map{it.toToiletInfo()}
        }catch(e: IOException){
            e.printStackTrace()
            return Resource.Error(message = "서버와 연결할 수 없습니다")
        }catch(e: HttpException){
            e.printStackTrace()
            return Resource.Error(message = "데이터를 가져올 수 없습니다")
        }
        return Resource.Success(data)
    }
}

private val testData = listOf(
    ToiletDto(
        POI_ID = "102423",
        FNAME = "우성스포츠센터",
        ANAME = "민간개방화장실",
        CNAME = "",
        CENTER_X1 = 192026.077328,
        CENTER_Y1 = 443662.903901,
        X_WGS84 = 126.90983237468618,
        Y_WGS84 = 37.49238614627172,
        INSERTDATE = "20100712",
        UPDATEDATE = "20100712"
    ),
    ToiletDto(
        POI_ID = "102424",
        FNAME = "프레곤빌딩",
        ANAME = "민간개방화장실",
        CNAME = "",
        CENTER_X1 = 191560.448911,
        CENTER_Y1 = 442968.699196,
        X_WGS84 = 126.90457509912622,
        Y_WGS84 = 37.48612718078578,
        INSERTDATE = "20100712",
        UPDATEDATE = "20100712"
    ),
    ToiletDto(
        POI_ID = "102425",
        FNAME = "하림빌딩",
        ANAME = "민간개방화장실",
        CNAME = "",
        CENTER_X1 = 201472.042745,
        CENTER_Y1 = 443869.796509,
        X_WGS84 = 127.01664600669827,
        Y_WGS84 = 37.49428349959237,
        INSERTDATE = "20100712",
        UPDATEDATE = "20100712"
    ),
)