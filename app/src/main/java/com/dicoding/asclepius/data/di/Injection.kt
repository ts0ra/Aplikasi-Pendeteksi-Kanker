package com.dicoding.asclepius.data.di

import android.content.Context
import com.dicoding.asclepius.data.database.local.room.HistoryDatabase
import com.dicoding.asclepius.data.database.remote.retrofit.ApiConfig
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.data.repository.NewsRepository

object Injection {
    fun provideHistoryRepository(context: Context): HistoryRepository {
        val database = HistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        return HistoryRepository.getInstance(dao)
    }

    fun provideNewsRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        return NewsRepository.getInstance(apiService)
    }
}