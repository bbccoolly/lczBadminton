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
}