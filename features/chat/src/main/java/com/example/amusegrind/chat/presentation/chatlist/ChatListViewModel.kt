package com.example.amusegrind.chat.presentation.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amusegrind.chat.domain.ChatUseCase
import com.example.amusegrind.chat.domain.FetchUsersUseCase
import com.example.amusegrind.navigator.ChatMessagesDestination
import com.example.amusegrind.navigator.Navigator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
    class ChatListViewModel @Inject constructor(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val chatUseCase: ChatUseCase,
    private val navigator: Navigator,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val _state = MutableStateFlow<ChatListState>(ChatListState(listOf()))
    val state: StateFlow<ChatListState> = _state.asStateFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            fetchUsersUseCase()
                .catch { e ->
                    println("Error fetching users: ${e.message}")
                }
                .collect { userList ->
                    _state.update { it.copy(userList = userList) }
                }
        }
    }

    fun navigateToMessages(recieverId: String) =
        navigator.navigate(ChatMessagesDestination.createRoute(ID = recieverId))
}
