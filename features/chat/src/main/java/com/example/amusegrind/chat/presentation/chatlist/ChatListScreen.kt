package com.example.amusegrind.chat.presentation.chatlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amusegrind.network.domain.entities.user.User
import kotlin.reflect.KFunction1

@Composable
fun ChatListScreen() {
    val viewModel: ChatListViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.value.userList) { user ->
            UserItem(user = user, navigateToMessages = viewModel::navigateToMessages)
        }
    }
}

@Composable
fun UserItem(user: User, navigateToMessages: KFunction1<String, Boolean>) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            user.username?.let {
                Text(text = it, modifier = Modifier.weight(1f).clickable {
                    navigateToMessages(user.uid!!)
                })
            }
        }
    }
}
