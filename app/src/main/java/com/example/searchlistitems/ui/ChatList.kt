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

    var hasUserScrolledUp by remember { mutableStateOf(false) }
    var renderedMessages by remember { mutableStateOf<List<ChatMessage>>(emptyList()) }

    val isAtBottom by remember {
        derivedStateOf {
            if (renderedMessages.isEmpty()) true
            else {
                val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val lastIndex = renderedMessages.lastIndex
                lastVisibleIndex != null && lastVisibleIndex >= lastIndex
            }
        }
    }

    // Update rendered messages only when user hasn't scrolled up
    LaunchedEffect(messages) {
        if (!hasUserScrolledUp) {
            renderedMessages = messages
            if (renderedMessages.isNotEmpty()) {
                listState.animateScrollToItem(renderedMessages.lastIndex)
            }
        }
    }

    // When returning to bottom (by user or program), resume live feed
    LaunchedEffect(isAtBottom) {
        if (isAtBottom) {
            hasUserScrolledUp = false
            renderedMessages = messages
        }
    }

    // Detect user scroll up action
    LaunchedEffect(listState, renderedMessages) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.isScrollInProgress }
            .collectLatest { (firstIndex, isScrolling) ->
                if (isScrolling) {
                    val lastIndex = renderedMessages.lastIndex
                    if (lastIndex >= 0 && firstIndex < lastIndex) {
                        hasUserScrolledUp = true
                    }
                }
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
                items = renderedMessages,
                key = { it.hashCode() }
            ) { message ->
                ChatMessageItem(message = message)
            }
        }

        if (!isAtBottom && renderedMessages.isNotEmpty()) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        hasUserScrolledUp = false
                        renderedMessages = messages
                        if (renderedMessages.isNotEmpty()) {
                            listState.animateScrollToItem(renderedMessages.lastIndex)
                        }
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = message.username,
            color = Color(0xFF00FF00),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
        Text(
            text = ": ",
            color = Color.White,
            fontSize = 12.sp
        )
        Text(
            text = message.message,
            color = Color.White,
            fontSize = 12.sp
        )
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
        message = ChatMessage("PixelPenguin", "Hello everyone! This is a sample chat message for preview.")
    )
}
