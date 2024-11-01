package com.dicoding.asclepius.data.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("history")
data class HistoryEntity(
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("result")
    val result: String,

    @ColumnInfo("score")
    val score: String,

    @ColumnInfo("imagePath")
    val imagePath: String
)
