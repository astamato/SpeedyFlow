package com.example.searchlistitems.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchlistitems.data.ChatMessage
import kotlinx.coroutines.launch

@Composable
fun ChatList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Detect if the last item is visible → we're at the bottom
    val isAtBottom by remember(messages, listState) {
        derivedStateOf {
            if (messages.isEmpty()) true
            else listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == messages.lastIndex
        }
    }

    // Auto-scroll only when already at bottom
    LaunchedEffect(messages.size) {
        if (isAtBottom && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A)),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = messages,
                key = { it.hashCode() }
            ) { message ->
                ChatMessageItem(message = message)
            }
        }

        // Show FAB only when not at bottom
        if (!isAtBottom && messages.isNotEmpty()) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(messages.lastIndex)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Scroll to bottom",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    val isMine = message.username == "You"
    val nameColor = if (isMine) Color(0xFFFF9800) else Color(0xFF00FF00)
    val bubbleColor = if (isMine) Color(0x332196F3) else Color.Transparent

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = message.username,
            color = nameColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
        Text(
            text = ": ",
            color = Color.White,
            fontSize = 12.sp
        )
        Box(
            modifier = Modifier
                .padding(vertical = 1.dp, horizontal = 0.dp)
                .background(bubbleColor)
        ) {
            Text(
                text = message.message,
                color = Color.White,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ChatListPreview() {
    ChatList(
        messages = ChatMessage.sampleMessages
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ChatMessageItemPreview() {
    ChatMessageItem(
        message = ChatMessage("You", "This is my highlighted message")
    )
}
