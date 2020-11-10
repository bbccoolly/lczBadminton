package com.lcz.bm.entity

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-10
 */
data class SuccessEntity(
    val allpage: Int,
    val code: Int,
    val `data`: List<Any>,
    val msg: String,
    val pagenum: Int,
    val total: Any
)