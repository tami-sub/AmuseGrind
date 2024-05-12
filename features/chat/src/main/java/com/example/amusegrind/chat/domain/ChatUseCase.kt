package com.example.amusegrind.chat.domain

import com.example.amusegrind.network.data.ChatRepo
import com.example.amusegrind.network.domain.entities.chat.Message
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatUseCase @Inject constructor(private val chatRepo: ChatRepo){

    fun sendMessage(senderRoom: String, receiverRoom: String, messageText: String, senderUid: String) {
        val message = Message(messageText, senderUid)
        chatRepo.sendMessage(senderRoom, receiverRoom, message)
    }

    fun receiveMessages(roomId: String): Flow<List<Message>> {
        return chatRepo.receiveMessages(roomId)
    }
}
