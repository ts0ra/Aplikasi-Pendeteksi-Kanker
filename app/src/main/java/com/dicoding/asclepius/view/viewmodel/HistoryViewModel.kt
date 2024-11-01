package com.dicoding.asclepius.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.database.local.entity.HistoryEntity
import com.dicoding.asclepius.data.repository.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {



    fun getAllHistory() = historyRepository.getAllHistory()
    fun addHistory(history: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.insertHistory(history)
        }
    }
    fun deleteHistory(history: HistoryEntity) {
        viewModelScope.launch {
            historyRepository.deleteHistory(history)
        }

    }
}