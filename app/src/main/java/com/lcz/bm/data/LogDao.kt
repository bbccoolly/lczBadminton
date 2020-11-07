package com.lcz.bm.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
@Dao
interface LogDao {
    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun getAll(): List<LogEntity>

    @Insert
    fun insertAll(vararg logs: LogEntity)

    @Query("DELETE FROM logs")
    fun nukeTable()

    @Query("SELECT * FROM logs ORDER BY id DESC")
    fun selectAllLogsCursor(): Cursor

    @Query("SELECT * FROM logs WHERE id = :id")
    fun selectLogById(id: Long): Cursor?
}