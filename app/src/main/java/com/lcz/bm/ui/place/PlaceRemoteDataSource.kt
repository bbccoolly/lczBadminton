package com.lcz.bm.ui.place

import com.lcz.bm.api.LczBMService
import com.lcz.bm.entity.CheckSelectEntity
import com.lcz.bm.entity.PlaceEntity
import com.lcz.bm.net.Result
import com.lcz.bm.net.safeApiCall
import java.io.IOException
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
class PlaceRemoteDataSource @Inject constructor(
    private val service: LczBMService
) {
    suspend fun getPlaceList(token: String, selectTime: String) = safeApiCall(
        call = { requestPlaceList(token, selectTime) },
        errorMessage = "网络连接异常"
    )

    suspend fun checkSelectPlace(token: String, selectTime: String) = safeApiCall(
        call = { requestCheckSelectPlace(token, selectTime) },
        errorMessage = "网络连接异常"
    )

    private suspend fun requestPlaceList(token: String, selectTime: String): Result<PlaceEntity> {
        val response = service.getPlaceList(
            mapOf(
                "api_version" to "5",
                "platform" to "1",
                "token" to token
            ),
            mapOf(
                "date" to selectTime,
                "sportId" to "4028f0ce5551abf3015551b0aae50001",
                "departId" to "1418"
            )
        )
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) {
                if (result.isSuccess) {
                    val data = result.data
                    if (data != null) {
                        return Result.Success(data = data)
                    }
                } else {
                    return Result.Error(IOException(result.msg))
                }
            }
        }
        return Result.Error(IOException("Access token retrieval failed ${response.code()} ${response.message()}"))
    }

    private suspend fun requestCheckSelectPlace(
        token: String,
        selectTime: String
    ): Result<CheckSelectEntity> {
        val response = service.checkSelectPlace(
            mapOf(
                "api_version" to "5",
                "platform" to "1",
                "token" to token
            ),
            mapOf(
                "fieldDate" to selectTime,
                "fieldDetailIdsList" to "6679,6680"
            )
        )
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) {
                if (result.isSuccess) {
                    val data = result.data
                    if (data != null) {
                        return Result.Success(data = data)
                    }
                } else {
                    return Result.Error(IOException(result.msg))
                }
            }
        }
        return Result.Error(IOException("Access token retrieval failed ${response.code()} ${response.message()}"))
    }
}