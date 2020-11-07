package com.lcz.bm.data

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
@Database(entities = arrayOf(LogEntity::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun logDao(): LogDao
}