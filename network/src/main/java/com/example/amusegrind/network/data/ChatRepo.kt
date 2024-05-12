package com.example.amusegrind.network.data

import com.example.amusegrind.network.domain.entities.chat.Message
import com.example.amusegrind.network.domain.entities.user.User
import kotlinx.coroutines.flow.Flow

interface ChatRepo {
    fun fetchUsers(): Flow<List<User>>
    fun sendMessage(senderRoom: String, receiverRoom: String, message: Message)
    fun receiveMessages(roomId: String): Flow<List<Message>>
}