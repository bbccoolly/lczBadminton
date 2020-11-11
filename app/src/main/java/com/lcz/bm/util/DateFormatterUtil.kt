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
    fun getAutoStartTimeString(): String {
        val calendar = Calendar.getInstance()
        val time = calendar.time
        return formatter2.format(time) + " 09:00:00"
    }

    //获取当前订单开始时间
    fun getAutoSelectTImeLong(): Long {
        return formatter2.parse(getAutoStartTimeString()).time
    }

    //获取某天场地信息
    fun getDayFieldPlaceTime(type: Int): String {
        val calendar = Calendar.getInstance()
        when (type) {
            2 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +1)//当前时间一天后
            }
            3 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +2)//当前时间一天后
            }
            4 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +3)//当前时间一天后
            }
            5 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +4)//当前时间一天后
            }
            6 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +5)//当前时间一天后
            }
            7 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +6)//当前时间一天后
            }
        }
        val time = calendar.time
        return formatter2.format(time)
    }

    //获取某天场地信息
    fun getDayFieldPlaceTimeWeek(type: Int): String {
        val calendar = Calendar.getInstance()
        var week = "星期一"
        when (type) {
            2 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +1)//当前时间一天后
                week = "星期二"
            }
            3 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +2)//当前时间一天后
                week = "星期三"
            }
            4 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +3)//当前时间一天后
                week = "星期四"
            }
            5 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +4)//当前时间一天后
                week = "星期五"
            }
            6 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +5)//当前时间一天后
                week = "星期六"
            }
            7 -> {
                calendar.add(Calendar.DAY_OF_MONTH, +6)//当前时间一天后
                week = "星期天"
            }
        }
        val time = calendar.time
        return "预定场地时间为：" + formatter2.format(time) + " " + week + " 晚8点到10点"
    }

}