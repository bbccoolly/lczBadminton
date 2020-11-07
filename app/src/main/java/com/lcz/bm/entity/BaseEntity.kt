package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class BaseEntity(
    val allpage: Any,
    val code: Int,
    val `data`: Any,
    val msg: String,
    val pagenum: Any,
    val total: Any
) : Serializable