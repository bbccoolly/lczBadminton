package com.lcz.bm.ui.order

import android.util.Log
import com.lcz.bm.api.LczBMService
import com.lcz.bm.entity.ResultEntity
import com.lcz.bm.entity.SubmitEntity
import com.lcz.bm.entity.SuccessEntity
import com.lcz.bm.net.Result
import com.lcz.bm.net.safeApiCall
import com.lcz.bm.util.GsonUtil
import com.lcz.bm.util.ProvideOrderDataUtil
import java.io.IOException
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
class OrderRemoteDataSource @Inject constructor(
    private val service: LczBMService,
    private val gson: GsonUtil
) {
    suspend fun getOrderList(token: String) = safeApiCall(
        call = { requestGetOrderList(token) },
        errorMessage = "网络连接异常"
    )

    suspend fun submitOrder(token: String, submitEntity: SubmitEntity) = safeApiCall(
        call = { requestSubmitOrder(token, submitEntity) },
        errorMessage = "网络连接异常"
    )

    private suspend fun requestGetOrderList(token: String): Result<SuccessEntity> {
        val response = service.getOrderList(
            mapOf(
                "api_version" to "5",
                "platform" to "1",
                "token" to token
            ),
            mapOf(
                "status" to "0",
                "curPage" to "1"
            )
        )
        if (response.isSuccessful) {
            val result = response.body()
            if (result != null) {
                when (result.code) {
                    900 -> {
                        return Result.Success(data = result)

                    }
                    906 -> {
                        return Result.ErrorReLogin(true, IOException(result.msg))
                    }
                    else -> {
                        return Result.Error(IOException(result.msg))
                    }
                }
            }
        }
        return Result.Error(IOException("Access token retrieval failed ${response.code()} ${response.message()}"))
    }

    private suspend fun requestSubmitOrder(
        token: String,
        submitEntity: SubmitEntity
    ): Result<ResultEntity> {
        val response = service.submitOrder(
            mapOf(
                "api_version" to "5",
                "platform" to "1",
                "token" to token
            ),
            gson.map2Body(submitEntity)
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