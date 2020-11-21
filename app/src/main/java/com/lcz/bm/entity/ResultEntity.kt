package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-10
 */
data class ResultEntity(
    val amount: String,
    val isFree: String,
    val ordernum: String,
    val remainTime: String
) : Serializable