package com.dicoding.asclepius.view.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.di.Injection
import com.dicoding.asclepius.data.repository.HistoryRepository
import com.dicoding.asclepius.data.repository.NewsRepository

class ViewModelFactory private constructor(
    private val historyRepository: HistoryRepository,
    private val newsRepository: NewsRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(historyRepository) as T
        } else if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(newsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val history = Injection.provideHistoryRepository(context)
                val news = Injection.provideNewsRepository(context)
                instance ?: ViewModelFactory(history, news)
            }.also { instance = it }

    }
}