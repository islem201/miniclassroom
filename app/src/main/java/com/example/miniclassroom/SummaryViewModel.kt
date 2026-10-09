package com.example.miniclassroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed interface SummaryState {
    object Idle : SummaryState
    object Loading : SummaryState
    data class Success(val text: String) : SummaryState
    data class Error(val message: String) : SummaryState
}

class SummaryViewModel : ViewModel() {
    var state by mutableStateOf<SummaryState>(SummaryState.Idle)
        private set

    fun summarize(content: String) {
        state = SummaryState.Loading
        viewModelScope.launch {
            state = try {
                SummaryState.Success(AiApi.summarize(content))
            } catch (e: Exception) {
                SummaryState.Error("Erreur : ${e.message}")
            }
        }
    }
}