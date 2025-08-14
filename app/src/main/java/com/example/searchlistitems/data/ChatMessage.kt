package com.example.searchlistitems.data

import java.time.LocalDateTime

data class ChatMessage(
    val username: String,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    val displayText: String
        get() = "$username: $message"

    companion object {
        val sampleMessages = listOf(
            ChatMessage("PixelPenguin", "Hello everyone!"),
            ChatMessage("CodeMaster42", "This stream is amazing!"),
            ChatMessage("KotlinQueen", "PogChamp"),
            ChatMessage("JetpackJake", "What a play!"),
            ChatMessage("AndroidDev", "Quality content right here"),
            ChatMessage("ComposeKing", "Let's go!"),
            ChatMessage("FlutterFan", "Actually insane"),
            ChatMessage("SwiftDev", "GG!"),
            ChatMessage("ReactRider", "This is fire!"),
            ChatMessage("VueVoyager", "MonkaS"),
            ChatMessage("AngularAce", "FeelsGoodMan"),
            ChatMessage("TypeScriptTitan", "PepeHands")
        )
    }
}
