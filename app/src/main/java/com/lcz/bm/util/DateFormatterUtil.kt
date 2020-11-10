package com.lcz.bm.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DateFormatterUtil @Inject constructor() {
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @SuppressLint("SimpleDateFormat")
    private val formatter2 = SimpleDateFormat("yyyy-MM-dd")

    fun formatDateDay(timestamp: Long): String {
        return formatter2.format(Date(timestamp))
    }

    fun formatDate(timestamp: Long): String {
        return formatter.format(Date(timestamp))
    }

    fun getData(timeS: String): Long {
        return formatter.parse(timeS).time
    }

    //获取当前系统时间
    fun getCurrentTimeString(): String {
        return formatter.format(System.currentTimeMillis())
    }

    //获取当前系统时间
    fun getCurrentTimeLong(): Long {
        return formatter.parse(getCurrentTimeString()).time
    }


    //自动抢单时间
    fun getStartTimeString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, +1)//当前时间一天后
        val time = calendar.time
        return formatter2.format(time) + " 09:00:00"
    }

    //获取当前订单开始时间
    fun getSelectTImeLong(): Long {
        return formatter2.parse(getStartTimeString()).time
    }

    //自动抢单时间
    fun getStartNetTimeString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, +1)//当前时间一天后
        val time = calendar.time
        return formatter2.format(time)
    }


}