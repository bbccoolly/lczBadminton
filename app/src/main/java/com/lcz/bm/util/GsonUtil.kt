package com.lcz.bm.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
class GsonUtil @Inject constructor(){
    fun map2Body(`object`: Any): RequestBody {
        val data = bean2Json(`object`)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), data)
    }

    fun bean2Json(`object`: Any): String {
        val gson = gson
        return gson.toJson(`object`)
    }

    fun <T> json2Bean(result: String?, clazz: Class<T>?): T {
        val gson = gson
        return gson.fromJson(result, clazz)
    }

    fun <T> jsonRawBean(result: String?, clazz: Class<T>?): T {
        val gson = Gson()
        return gson.fromJson(result, clazz)
    }


    fun <T> array2Json(lists: List<T>?): String {
        val listType = object : TypeToken<List<T>?>() {}.type
        val gson = gson
        return gson.toJson(lists, listType)
    }

    fun <T> json2Array(result: String?, typeToken: TypeToken<List<T>?>): List<T> {
        val gson = gson
        return gson.fromJson(result, typeToken.type)
    }

    fun <T> jsonRawArray(result: String?, typeToken: TypeToken<List<T>?>): List<T> {
        val gson = Gson()
        return gson.fromJson(result, typeToken.type)
    }

    fun json2Map(result: String?): Map<*, *>? {
        val gson = gson
        return gson.fromJson<Map<*, *>>(result, MutableMap::class.java)
    }

    val gson: Gson
        //        get() = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        get() = GsonBuilder().create()


}