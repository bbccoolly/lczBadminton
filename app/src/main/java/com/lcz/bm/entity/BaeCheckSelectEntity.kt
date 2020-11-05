package com.lcz.bm.entity

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class BaseCheckSelectEntity(
    val allpage: Any,
    val code: Int,
    val `data`: CSData,
    val msg: String,
    val pagenum: Any,
    val total: Any
)

data class CSData(
    val canOrder: Boolean,
    val fieldHours: String,
    val fieldMaxHours: String,
    val orderHours: String
)