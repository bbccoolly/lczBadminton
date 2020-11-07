package com.lcz.bm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * desc: TODO
 *
 * create by Arrow on 2020-11-07
 */
@Entity(tableName = "logs")
data class LogEntity(val msg: String, val timestamp: Long) {
    @PrimaryKey
    var id: Long = 0
}