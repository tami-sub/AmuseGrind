package com.example.amusegrind.network

import android.util.Log
import com.example.amusegrind.network.data.ChatRepo
import com.example.amusegrind.network.domain.entities.chat.Message
import com.example.amusegrind.network.domain.entities.user.User
import com.example.amusegrind.network.utils.FirePath
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : ChatRepo {
    private val databaseReference = firebaseDatabase.reference
    override fun fetchUsers(): Flow<List<User>> = callbackFlow {
        val listener =
            databaseReference.child(FirePath.getAllUsersPath())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users = mutableListOf<User>()
                        snapshot.children.forEach { child ->
                            child.child(FirePath.getAllUserBasicDataPath())
                                .getValue(User::class.java)?.let { user ->
                                if (firebaseAuth.currentUser?.uid != user.uid) {
                                    users.add(user)
                                }
                            }
                        }
                        trySend(users).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(Exception(error.message))
                    }
                })

        awaitClose { databaseReference.removeEventListener(listener) }
    }


    private val mDbRef = FirebaseDatabase.getInstance().reference

    override fun sendMessage(senderRoom: String, receiverRoom: String, message: Message) {
        mDbRef.child(FirePath.getChatsPath()).child(senderRoom).child(FirePath.getMessagesPath())
            .push()
            .setValue(message)
            .addOnSuccessListener {
                mDbRef.child(FirePath.getChatsPath()).child(receiverRoom)
                    .child(FirePath.getMessagesPath()).push()
                    .setValue(message)
            }
    }

    override fun receiveMessages(roomId: String): Flow<List<Message>> = callbackFlow {
        val listener =
            mDbRef.child(FirePath.getChatsPath()).child(roomId).child(FirePath.getMessagesPath())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val messages =
                            snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                        trySend(messages).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(error.toException())
                    }
                })

        awaitClose {
            mDbRef.removeEventListener(listener)
        }
    }
}
