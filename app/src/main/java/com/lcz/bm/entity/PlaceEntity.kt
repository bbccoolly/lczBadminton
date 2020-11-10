package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: 场地信息
 *
 * create by Arrow on 2020-11-10
 */
data class PlaceEntity(
    val allpage: Any,
    val code: Int,
    val `data`: List<Place>,
    val msg: String,
    val pagenum: Any,
    val total: Any
)

data class Place(
    val coupons: List<Any>,
    val fieldList: List<Field>,
    val maxMinutes: Double,
    val maxTime: Int,
    val minMinutes: Double,
    val minTime: Int,
    val platform: String,
    val singleMinMinutes: Double,
    val singleMinTime: Int,
    val type: String,
    val typeName: String
)

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
)

data class Price(
    val endTime: String,
    val id: Int,
    val price: String,
    val seriesId: String,
    val startTime: String,
    val status: String
)