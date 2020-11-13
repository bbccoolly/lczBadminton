package com.lcz.bm.net

import java.io.Serializable

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
class BaseHttpResult<T> : Serializable {

    var code: Int? = null
    var data: T? = null
    var msg: String? = null

    val isSuccess: Boolean
        get() = SUCCESS_CODE == code

    val isReLogin: Boolean
        get() = SUCCESS_RELOGIN == code

    companion object {
        private const val SUCCESS_CODE = 900
        private const val SUCCESS_RELOGIN = 906
    }
}