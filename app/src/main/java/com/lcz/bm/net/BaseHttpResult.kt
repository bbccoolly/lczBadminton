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

    companion object {
        private const val SUCCESS_CODE = 900
    }
}