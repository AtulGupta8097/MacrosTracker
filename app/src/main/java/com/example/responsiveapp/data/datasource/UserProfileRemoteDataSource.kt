package com.example.responsiveapp.data.datasource

import com.example.responsiveapp.data.remote.dto.UserProfileDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun save(dto: UserProfileDto) {
        val uid = auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        firestore
            .collection("users")
            .document(uid)
            .set(dto)
            .await()
    }

    suspend fun get(): UserProfileDto? {
        val uid = auth.currentUser?.uid ?: return null

        val snapshot = firestore
            .collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserProfileDto::class.java)
    }
}