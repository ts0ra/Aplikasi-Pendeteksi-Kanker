package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.Result
import com.dicoding.asclepius.data.database.remote.response.ArticlesItem
import com.dicoding.asclepius.data.database.remote.retrofit.ApiService

class NewsRepository private constructor(
    private val apiService: ApiService
) {

    fun getNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews()
            val articles = response.articles?.filterNotNull() ?: emptyList()
            emit(Result.Success(articles))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService)
            }.also { instance = it }
    }
}