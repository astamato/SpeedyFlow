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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.searchlistitems.data.ChatMessage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun ChatList(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Stable flag: when true, we keep snapping to bottom on new messages
    var autoScroll by remember { mutableStateOf(true) }

    // Observe user scroll and list visibility to toggle autoScroll without flicker
    LaunchedEffect(listState, messages.size) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            // Consider "at bottom" if the last visible is within 1 item of the end
            val nearBottom = if (messages.isEmpty()) true else lastVisible >= messages.lastIndex - 1
            listState.isScrollInProgress to nearBottom
        }.collectLatest { (scrolling, nearBottom) ->
            if (scrolling) {
                if (!nearBottom) autoScroll = false
            } else {
                if (nearBottom) autoScroll = true
            }
        }
    }

    // Only scroll when autoScroll is enabled (snap, not animate, to avoid rapid animation thrash)
    LaunchedEffect(messages.size, autoScroll) {
        if (autoScroll && messages.isNotEmpty()) {
            listState.scrollToItem(messages.lastIndex)
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

        // Show FAB only when auto-scroll is disabled (i.e., user scrolled up)
        if (!autoScroll && messages.isNotEmpty()) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        autoScroll = true
                        // Animate only on explicit user intent
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
