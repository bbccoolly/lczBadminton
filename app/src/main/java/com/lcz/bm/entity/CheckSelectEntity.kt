package com.lcz.bm.entity

/**
 *
 * desc: 校验场地
 *
 * create by Arrow on 2020-11-10
 */
data class CheckSelectEntity(
    val canOrder: Boolean,
    val fieldHours: String,
    val fieldMaxHours: String,
    val orderHours: String
)