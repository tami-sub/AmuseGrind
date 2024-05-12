package com.example.amusegrind.chat.presentation

import com.example.amusegrind.network.domain.entities.chat.Message

data class ChatState(val chatMessages: List<Message>, val uid: String?)
