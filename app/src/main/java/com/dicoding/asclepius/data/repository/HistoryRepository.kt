package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.database.local.entity.HistoryEntity
import com.dicoding.asclepius.data.database.local.room.HistoryDao
import java.io.File

class HistoryRepository private constructor(private val historyDao: HistoryDao) {

    fun getAllHistory(): LiveData<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }

    suspend fun insertHistory(history: HistoryEntity) {
        historyDao.insertHistory(history)
    }

    suspend fun deleteHistory(history: HistoryEntity) {
        historyDao.deleteHistory(history)
        val file = File(history.imagePath)
        if (file.exists()) {
            file.delete()
        }
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun getInstance(historyDao: HistoryDao
        ): HistoryRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryRepository(historyDao)
            }.also { instance = it }
    }
}