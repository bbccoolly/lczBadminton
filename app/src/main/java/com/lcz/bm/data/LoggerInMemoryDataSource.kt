package com.lcz.bm.data

import java.util.*
import javax.inject.Inject

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
class LoggerInMemoryDataSource @Inject constructor() : LoggerDataSource {

    private val logs = LinkedList<LogEntity>()

    override fun addLog(msg: String) {
        logs.addFirst(LogEntity(msg, System.currentTimeMillis()))
    }

    override fun getAllLogs(callback: (List<LogEntity>) -> Unit) {
        callback(logs)
    }

    override fun removeLogs() {
        logs.clear()
    }

}