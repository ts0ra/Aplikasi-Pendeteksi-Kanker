package com.dicoding.asclepius.view.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.repository.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {
    fun getNews() = newsRepository.getNews()
}