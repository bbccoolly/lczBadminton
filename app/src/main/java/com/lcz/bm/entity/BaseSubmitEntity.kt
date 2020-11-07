package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class BaseSubmitEntity(
    val allpage: Any,
    val code: Int,
    val `data`: SData,
    val msg: String,
    val pagenum: Any,
    val total: Any
) : Serializable

data class SData(
    val amount: String,
    val isFree: String,
    val ordernum: String,
    val remainTime: String
) : Serializable