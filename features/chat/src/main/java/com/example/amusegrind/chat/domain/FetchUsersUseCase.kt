package com.example.amusegrind.chat.domain

import com.example.amusegrind.network.data.ChatRepo
import com.example.amusegrind.network.domain.entities.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(private val chatRepo: ChatRepo) {
    operator fun invoke(): Flow<List<User>> {
        return chatRepo.fetchUsers()
            .map { users ->
                // Apply any business rules if necessary
                // For example, sorting the users alphabetically by name
                users.sortedBy { it.username }
            }
    }
}
