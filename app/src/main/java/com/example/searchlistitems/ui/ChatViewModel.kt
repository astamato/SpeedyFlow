package com.example.searchlistitems.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchlistitems.data.ChatMessage
import com.example.searchlistitems.data.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val chatMessages: StateFlow<List<ChatMessage>> = repository.messagesFlow
    val skippedMessagesCount: StateFlow<Int> = repository.skippedMessagesCount

    private val _isChatPaused = MutableStateFlow(false)
    val isChatPaused: StateFlow<Boolean> = _isChatPaused

    private val _memoryInfo = MutableStateFlow("Memory: 0 displayed, 0 buffered, max: 500")
    val memoryInfo: StateFlow<String> = _memoryInfo

    // Input state
    val inputText = MutableStateFlow("")

    init {
        repository.startGenerating(viewModelScope)

        // Update memory info periodically
        viewModelScope.launch {
            while (true) {
                _memoryInfo.value = repository.getMemoryInfo()
                delay(5000) // Update every 5 seconds
            }
        }
    }

    fun toggleChatPause() {
        _isChatPaused.update { !it }
        if (_isChatPaused.value) {
            repository.stopGenerating()
        } else {
            repository.startGenerating(viewModelScope)
        }
    }

    fun resetSkippedCount() {
        repository.resetSkippedCount()
    }

    fun sendMessage() {
        val text = inputText.value.trim()
        if (text.isNotEmpty()) {
            repository.sendUserMessage(text)
            inputText.value = ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.stopGenerating()
    }
}
