package com.example.searchlistitems.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ChatRepository {
    private val messageBuffer = mutableListOf<ChatMessage>()

    // Memory management configuration
    private val maxMessagesInMemory = 500 // Maximum messages to keep in memory
    private val maxMessageAgeMinutes = 10 // Remove messages older than 10 minutes
    private val cleanupIntervalMs = 30000L // Clean up every 30 seconds

    private val _messagesFlow = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messagesFlow: StateFlow<List<ChatMessage>> = _messagesFlow

    private val _skippedMessagesCount = MutableStateFlow(0)
    val skippedMessagesCount: StateFlow<Int> = _skippedMessagesCount

    private var isGenerating = false

    private val usernames = listOf(
        "PixelPenguin", "CodeMaster42", "KotlinQueen", "JetpackJake",
        "AndroidDev", "ComposeKing", "FlutterFan", "SwiftDev",
        "ReactRider", "VueVoyager", "AngularAce", "TypeScriptTitan",
        "PythonPro", "JavaJedi", "CSharpChamp", "GoGuru",
        "RustRacer", "DartDev", "KotlinKnight", "SwiftSage"
    )

    private val messages = listOf(
        "Hello!", "GG!", "This is amazing", "LOL", "No way!", "Clip that!",
        "PogChamp", "Kappa", "MonkaS", "FeelsGoodMan", "PepeHands",
        "What a play!", "Insane!", "Unbelievable!", "Wow!", "Nice!",
        "Let's go!", "EZ Clap", "PogU", "Actually insane",
        "This stream is fire", "Quality content", "Love this game",
        "Subscribed!", "Donated!", "Followed!", "Raid incoming!",
        "ModCheck", "Ban this guy", "Timeout", "Welcome everyone!",
        "First time here", "Long time viewer", "New to the community",
        "Thanks for the raid!", "Good vibes only", "Positive chat",
        "Respect the streamer", "Be kind", "Spread love", "Wholesome"
    )

    fun startGenerating(scope: CoroutineScope) {
        if (isGenerating) return
        isGenerating = true

        // Simulate incoming chat messages
        scope.launch {
            while (isGenerating) {
                val message = ChatMessage(
                    username = usernames.random(),
                    message = messages.random()
                )

                synchronized(messageBuffer) {
                    messageBuffer.add(message)

                    // Remove oldest messages if we exceed the limit
                    while (messageBuffer.size > maxMessagesInMemory) {
                        messageBuffer.removeAt(0)
                        _skippedMessagesCount.update { it + 1 }
                    }
                }
                delay(20) // ~50 msg/sec
            }
        }

        // Emit batches to UI
        scope.launch {
            while (isGenerating) {
                val batch = synchronized(messageBuffer) {
                    val copy = messageBuffer.toList()
                    messageBuffer.clear()
                    copy
                }
                if (batch.isNotEmpty()) {
                    _messagesFlow.update { old ->
                        (old + batch).takeLast(maxMessagesInMemory)
                    }
                }
                delay(66) // ~15 fps
            }
        }

        // Periodic cleanup of old messages
        scope.launch {
            while (isGenerating) {
                cleanupOldMessages()
                delay(cleanupIntervalMs)
            }
        }
    }

    // Allow user-typed messages to be enqueued into the stream
    fun sendUserMessage(text: String, username: String = "You") {
        if (text.isBlank()) return
        val userMessage = ChatMessage(username = username, message = text.trim())
        synchronized(messageBuffer) {
            messageBuffer.add(userMessage)
            while (messageBuffer.size > maxMessagesInMemory) {
                messageBuffer.removeAt(0)
                _skippedMessagesCount.update { it + 1 }
            }
        }
    }

    private fun cleanupOldMessages() {
        val cutoffTime = LocalDateTime.now().minus(maxMessageAgeMinutes.toLong(), ChronoUnit.MINUTES)

        synchronized(messageBuffer) {
            val initialSize = messageBuffer.size
            messageBuffer.removeAll { it.timestamp.isBefore(cutoffTime) }
            val removedCount = initialSize - messageBuffer.size

            if (removedCount > 0) {
                _skippedMessagesCount.update { it + removedCount }
            }
        }

        // Also clean up the emitted flow
        _messagesFlow.update { currentMessages ->
            currentMessages.filter { !it.timestamp.isBefore(cutoffTime) }
        }
    }

    fun stopGenerating() {
        isGenerating = false
    }

    fun resetSkippedCount() {
        _skippedMessagesCount.value = 0
    }

    // Get current memory usage info
    fun getMemoryInfo(): String {
        val currentMessages = _messagesFlow.value.size
        val bufferSize = synchronized(messageBuffer) { messageBuffer.size }
        return "Memory: $currentMessages displayed, $bufferSize buffered, max: $maxMessagesInMemory"
    }
}
