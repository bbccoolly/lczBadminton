package com.lcz.bm.api

import com.lcz.bm.entity.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
interface BMApiService {

    @Headers("User-Agent:Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00)")
    @POST("user/login")
    @FormUrlEncoded
    fun login(@FieldMap map: Map<String, String>): Call<BaseUserEntity>

    //获取场地列表
    @Headers("User-Agent:Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00)")
    @GET("field/list")
    fun getPlaceList(
        @HeaderMap headers: Map<String, String>,
        @QueryMap map: Map<String, String>
    ): Call<BasePlaceEntity>

    //校验场地
    @Headers("User-Agent:Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00)")
    @GET("order/check/orderLimit")
    fun checkSelectPlace(
        @HeaderMap headers: Map<String, String>,
        @QueryMap map: Map<String, String>
    ): Call<BaseCheckSelectEntity>

    //提交订单
    @Headers("User-Agent:Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00)")
    @POST("order/place")
    fun submitOrder(
        @HeaderMap headers: Map<String, String>,
        @Body body: RequestBody
    ): Call<BaseSubmitEntity>

    //用于校验账号是否处于登录状态
    @Headers("User-Agent:Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00)")
    @POST("order/list")
    @FormUrlEncoded
    fun getOrderList(
        @HeaderMap headers: Map<String, String>,
        @FieldMap map: Map<String, String>
    ): Call<BaseEntity>
}