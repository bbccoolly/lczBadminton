package com.lcz.bm.entity

import java.io.Serializable

/**
 *
 * desc: 用户信息
 *
 * create by Arrow on 2020-11-09
 */
data class LoginUserEntity(
    val hasPassword: Boolean,
    val id: Int,
    val registerSendPoints: Int,
    val token: String
) : Serializable