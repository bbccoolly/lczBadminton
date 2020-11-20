package com.lcz.bm.ui.login

import com.lcz.bm.api.LczBMService
import com.lcz.bm.entity.LoginUserEntity
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
class LoginRemoteDataSource @Inject constructor(
    private val service: LczBMService
) {
    suspend fun login() = safeApiCall(
        call = { requestLogin() },
        errorMessage = "网络连接异常"
    )

    private suspend fun requestLogin(): Result<LoginUserEntity> {
        val response = service.login(
            mapOf(
                "phone" to "18925287073",//"13530318471",//18925287073
                "password" to "412430",//"meng520",//412430
                "inviteCode" to "",
                "source" to "1",
                "api_version" to "5",
                "platform" to "1",
                "token" to ""
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