package com.lcz.bm.entity

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class SubmitEntity(
    val fieldorder: Fieldorder,
    val ordertype: String,
    val sportid: String,
    val venueid: String
)

data class Fieldorder(
    val date: String,
    val fieldlist: ArrayList<Fieldlist>
)

data class Fieldlist(
    val etime: String,
    val id: String,
    val price: String,
    val priceidlist: ArrayList<String>,
    val stime: String
)