package com.example.amusegrind.chat.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.chat.domain.ChatUseCase
import com.example.amusegrind.navigator.ChatMessagesDestination
import com.example.amusegrind.navigator.Navigator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCase,
    private val navigator: Navigator,
    private val firebaseAuth: FirebaseAuth,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<ChatState>(ChatState(chatMessages = listOf(), uid = firebaseAuth.uid))
    val state: StateFlow<ChatState> = _state.asStateFlow()
    private val recieverId = ChatMessagesDestination.extractId(savedStateHandle = savedStateHandle)

    init {
        receiveMessages()
    }

    fun receiveMessages() {
        viewModelScope.launch {
            try {
                chatUseCase.receiveMessages("${firebaseAuth.uid}$recieverId").collect { messages ->
                    _state.update { it.copy(chatMessages = messages) }
                }
            } catch (e: Exception) {
                Log.d("joka", "Failed to recieve messages: ${e.message}")
            }
        }
    }

    fun sendMessage(
        messageText: String,
    ) {
        try {
            viewModelScope.launch {
                chatUseCase.sendMessage(
                    senderRoom = "${firebaseAuth.uid!!}${recieverId}",
                    receiverRoom = "${recieverId}${firebaseAuth.uid!!}",
                    messageText,
                    firebaseAuth.uid!!
                )
            }
        } catch (e: Exception){
            Log.d("joka", "Failed to send message: ${e.message}")
        }
    }
}