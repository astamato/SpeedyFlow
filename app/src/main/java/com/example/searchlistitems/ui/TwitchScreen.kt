package com.example.searchlistitems.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun TwitchScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = koinViewModel()
) {
    val chatMessages by chatViewModel.chatMessages.collectAsState()
    val skippedMessagesCount by chatViewModel.skippedMessagesCount.collectAsState()
    val isChatPaused by chatViewModel.isChatPaused.collectAsState()
    val memoryInfo by chatViewModel.memoryInfo.collectAsState()
    val inputText by chatViewModel.inputText.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFF0E0E10)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Video Player - fixed height, no weight
            VideoPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            // Chat and Controls - takes remaining space
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Control Panel
//                ControlPanel(
//                    isChatPaused = isChatPaused,
//                    skippedMessagesCount = skippedMessagesCount,
//                    memoryInfo = memoryInfo,
//                    onToggleChatPause = { chatViewModel.toggleChatPause() },
//                    onResetSkippedCount = { chatViewModel.resetSkippedCount() }
//                )

                // Chat List
                ChatList(
                    messages = chatMessages,
                    modifier = Modifier.weight(1f)
                )

                // Input bar
                ChatInputBar(
                    text = inputText,
                    onTextChange = { chatViewModel.inputText.value = it },
                    onSend = { chatViewModel.sendMessage() },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E10, showSystemUi = true)
@Composable
fun TwitchScreenPreview() {
    // Preview without DI
}
