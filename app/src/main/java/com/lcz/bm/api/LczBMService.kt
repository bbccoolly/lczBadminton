package com.lcz.bm.api

import com.lcz.bm.entity.*
import com.lcz.bm.net.BaseHttpResult
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
interface LczBMService {

    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(@FieldMap map: Map<String, String>): Response<BaseHttpResult<LoginUserEntity>>

    @GET("field/list")
    suspend fun getPlaceList(
        @HeaderMap headers: Map<String, String>,
        @QueryMap map: Map<String, String>
    ): Response<BaseHttpResult<PlaceEntity>>

    @GET("order/check/orderLimit")
    suspend fun checkSelectPlace(
        @HeaderMap headers: Map<String, String>,
        @QueryMap map: Map<String, String>
    ): Response<BaseHttpResult<CheckSelectEntity>>

    @POST("order/place")
    suspend fun submitOrder(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Response<BaseHttpResult<ResultEntity>>

    @POST("order/list")
    @FormUrlEncoded
    suspend fun getOrderList(
        @HeaderMap headers: Map<String, String>,
        @FieldMap map: Map<String, String>
    ): Response<BaseHttpResult<String>>

    companion object {
        private const val BASE_URL = "https://api.52jiayundong.com/"
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        fun create(): LczBMService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LczBMService::class.java)
        }
    }
}