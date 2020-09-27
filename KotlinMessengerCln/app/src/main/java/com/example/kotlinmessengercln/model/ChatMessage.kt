package com.example.kotlinmessengercln.model

import com.google.firebase.Timestamp

//Firestore Mesajlaşma(ChatMessage) veritabanı modeli
data class ChatMessage(

    val chatId: String? = null,
    val text: String? = null,
    val fromMessageId: String? = null,
    val toMessageId: String? = null,
    val date: Timestamp? = null

)