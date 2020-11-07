package com.lcz.bm.data

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

/**
 *
 * desc: between the database and the UI.
 *
 * create by Arrow on 2020-11-07
 */
class LoggerLocalDataSource @Inject constructor(private val logDao: LogDao) : LoggerDataSource {

    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val mainThreadHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    override fun addLog(msg: String) {
        executorService.execute {
            logDao.insertAll(
                LogEntity(msg, System.currentTimeMillis())
            )
        }
    }

    override fun getAllLogs(callback: (List<LogEntity>) -> Unit) {
        executorService.execute {
            val logs = logDao.getAll()
            mainThreadHandler.post { callback(logs) }
        }
    }

    override fun removeLogs() {
        executorService.execute {
            logDao.nukeTable()
        }
    }

}