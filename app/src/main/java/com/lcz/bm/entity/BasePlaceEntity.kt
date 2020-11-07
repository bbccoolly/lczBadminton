package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class BasePlaceEntity(
    val allpage: Any,
    val code: Int,
    val `data`: List<Data>,
    val msg: String,
    val pagenum: Any,
    val total: Any
) : Serializable

data class Data(
    val coupons: List<Any>,
    val fieldList: List<Field>,
    val maxMinutes: Int,
    val maxTime: Int,
    val minMinutes: Int,
    val minTime: Int,
    val platform: String,
    val singleMinMinutes: Int,
    val singleMinTime: Int,
    val type: String,
    val typeName: String
) : Serializable

data class Field(
    val fieldName: String,
    val fieldPeriod: Double,
    val fieldType: Any,
    val id: Int,
    val maxHours: String,
    val minHours: String,
    val minSeriesHours: String,
    val parentFieldId: Any,
    val priceList: List<Price>
) : Serializable

data class Price(
    val endTime: String,
    val id: Int,
    val price: String,
    val seriesId: String,
    val startTime: String,
    val status: String
) : Serializable