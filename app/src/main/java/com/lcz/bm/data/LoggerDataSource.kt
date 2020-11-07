package com.lcz.bm.data

import javax.security.auth.callback.Callback

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
interface LoggerDataSource {
    fun addLog(msg: String)
    fun getAllLogs(callback: (List<LogEntity>) -> Unit)
    fun removeLogs()
}