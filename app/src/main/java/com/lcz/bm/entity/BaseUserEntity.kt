package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-05
 */
data class BaseUserEntity(
    val allpage: Any,
    val code: Int,
    val `data`: UserEntity,
    val msg: String,
    val pagenum: Any,
    val total: Any
) : Serializable

data class UserEntity(
    val hasPassword: Boolean,
    val id: Int,
    val registerSendPoints: Int,
    val token: String,
    val user: User
) : Serializable

data class User(
    val age: Int,
    val area: Any,
    val avatar: String,
    val birthday: String,
    val city: Any,
    val contentRate: String,
    val createTime: String,
    val defaultImageIndex: Int,
    val departDiscountNum: Int,
    val energyVolumeNum: Int,
    val guessStatus: Int,
    val id: Int,
    val isAudit: Int,
    val isBlacklist: Boolean,
    val isVip: Int,
    val lastLoginTime: String,
    val loginNum: Int,
    val memberRate: String,
    val memberStatus: Int,
    val miniQrcode: String,
    val nickName: String,
    val password: Any,
    val phone: String,
    val province: Any,
    val rank: Int,
    val registerSource: Any,
    val remark: Any,
    val sex: Int,
    val signature: Any,
    val source: Int,
    val thisLoginTime: String,
    val venueId: Any
) : Serializable