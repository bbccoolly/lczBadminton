package com.lcz.bm.net

import java.io.IOException

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-09
 */
suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>, errorMessage: String): Result<T> {
    return try {
        call()
    } catch (e: Exception) {
        // An exception was thrown when calling the API so we're converting this to an IOException
        Result.Error(IOException(errorMessage, e))
    }
}