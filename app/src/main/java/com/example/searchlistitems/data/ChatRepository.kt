package com.example.searchlistitems.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ChatRepository {
  /**
   * High-rate event stream for chat messages (both simulator and user-sent).
   *
   * Backpressure policy:
   * - extraBufferCapacity provides a leaky bucket in front of collectors.
   * - onBufferOverflow = DROP_OLDEST evicts the oldest pending events when full.
   *   Those evicted events are not observed downstream and are not counted here.
   *
   * Guarantees/Trade-offs:
   * - Prevents producers from blocking under bursty load.
   * - May drop messages before they reach aggregation if bursts exceed capacity.
   */
  private val events = MutableSharedFlow<ChatMessage>(
    replay = 0,
    extraBufferCapacity = 1000,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
  )

  // Memory management configuration
  private val maxMessagesInMemory = 5000 // Maximum messages to keep in memory
  private val maxMessageAgeMinutes = 10 // Remove messages older than 10 minutes
  private val cleanupIntervalMs = 30000L // Clean up every 30 seconds

  private val _messagesFlow = MutableStateFlow<List<ChatMessage>>(emptyList())
  val messagesFlow: StateFlow<List<ChatMessage>> = _messagesFlow

  private val _skippedMessagesCount = MutableStateFlow(0)
  val skippedMessagesCount: StateFlow<Int> = _skippedMessagesCount

  private var isGenerating = false
  private var isCollectorStarted = false

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

  @OptIn(FlowPreview::class)
  fun startGenerating(scope: CoroutineScope) {
    if (isGenerating) return
    isGenerating = true

    // Start the flow collector once (off the main thread)
    if (!isCollectorStarted) {
      isCollectorStarted = true
      scope.launch(Dispatchers.Default) {
        events
          // Optional extra buffering to smooth bursts
          .buffer(capacity = 1024, onBufferOverflow = BufferOverflow.DROP_OLDEST)
          /**
           * Aggregate the high-rate event stream into a bounded UI list.
           * - runningFold accumulates the most recent [maxMessagesInMemory] messages
           *   using a simple tail-capped list.
           * - When the cap trims items, we increment [_skippedMessagesCount].
           * - This step does not drop messages by itself beyond the cap; earlier
           *   drops can still happen in [events] due to DROP_OLDEST.
           */
          .runningFold(emptyList<ChatMessage>()) { acc, msg ->
            val newList = acc + msg
            val overflow = (newList.size - maxMessagesInMemory).coerceAtLeast(0)
            if (overflow > 0) {
              _skippedMessagesCount.update { it + overflow }
            }
            newList.takeLast(maxMessagesInMemory)
          }
          // If collector/UI lags, keep only the most recent snapshot (UI cadence handled below)
          .conflate()
          // Deliver updates at a fixed UI cadence (~13 FPS) regardless of emit rate
          .sample(75)
          .collect { aggregated ->
            _messagesFlow.value = aggregated
          }
      }

      // Periodic cleanup of old messages from the displayed stream (background)
      scope.launch(Dispatchers.Default) {
        while (isGenerating) {
          cleanupOldMessages()
          delay(cleanupIntervalMs)
        }
      }
    }

    // Simulate incoming chat messages
    scope.launch(Dispatchers.Default) {
      while (isGenerating) {
        val message = ChatMessage(
          username = usernames.random(),
          message = messages.random()
        )
        events.tryEmit(message)
        delay(100) // adjustable rate
      }
    }
  }

  // Allow user-typed messages to be enqueued into the stream
  fun sendUserMessage(text: String, username: String = "You") {
    if (text.isBlank()) return
    val userMessage = ChatMessage(username = username, message = text.trim())
    events.tryEmit(userMessage)
  }

  private fun cleanupOldMessages() {
    val cutoffTime = LocalDateTime.now().minus(maxMessageAgeMinutes.toLong(), ChronoUnit.MINUTES)
    val beforeSize = _messagesFlow.value.size
    val filtered = _messagesFlow.value.filter { !it.timestamp.isBefore(cutoffTime) }
    _messagesFlow.value = filtered
    val removed = beforeSize - filtered.size
    if (removed > 0) {
      _skippedMessagesCount.update { it + removed }
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
    // events buffer size is not directly observable; report stream size
    return "Memory: $currentMessages displayed, cap: $maxMessagesInMemory"
  }
}
