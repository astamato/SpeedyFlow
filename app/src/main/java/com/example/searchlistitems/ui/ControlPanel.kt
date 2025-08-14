package com.example.searchlistitems.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ControlPanel(
    isChatPaused: Boolean,
    skippedMessagesCount: Int,
    memoryInfo: String,
    onToggleChatPause: () -> Unit,
    onResetSkippedCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Chat Controls",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Skipped Messages: $skippedMessagesCount",
                    color = Color(0xFFFF9800),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                TextButton(
                    onClick = onResetSkippedCount
                ) {
                    Text(
                        text = "Reset Counter",
                        color = Color(0xFF2196F3)
                    )
                }
            }

            // Memory info
            Text(
                text = memoryInfo,
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E10)
@Composable
fun ControlPanelPreview() {
    ControlPanel(
        isChatPaused = false,
        skippedMessagesCount = 42,
        memoryInfo = "Memory: 1.2GB / 4GB",
        onToggleChatPause = {},
        onResetSkippedCount = {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0E0E10)
@Composable
fun ControlPanelPausedPreview() {
    ControlPanel(
        isChatPaused = true,
        skippedMessagesCount = 0,
        memoryInfo = "Memory: 0.8GB / 4GB",
        onToggleChatPause = {},
        onResetSkippedCount = {}
    )
}
