package com.example.amusegrind.chat.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.amusegrind.chat.R
import com.example.amusegrind.network.domain.entities.chat.Message

@Composable
fun ChatScreen() {
    val viewModel: ChatViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize(), Arrangement.Bottom) {
        state.value.uid?.let { MessagesList(messages = state.value.chatMessages, currentUserUid = it) }
        MessageInputField(onMessageSent = { message ->
            viewModel.sendMessage(
                message
            )
        })
    }
}

@Composable
fun MessagesList(messages: List<Message>, currentUserUid: String) {
    LazyColumn(modifier = Modifier) {
        items(messages) { message ->
            message.message?.let {
                if (message.senderId!! == currentUserUid) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        textAlign = TextAlign.End
                    )
                } else {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageInputField(onMessageSent: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(bottom = 80.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = text,
            onValueChange = { newText -> text = newText },
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.type_a_message)) }
        )
        Button(
            onClick = {
                onMessageSent(text)
                text = ""
            },
            enabled = text.isNotBlank()
        ) {
            Text(stringResource(R.string.send))
        }
    }
}
