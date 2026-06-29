package com.example.responsiveapp.domain.session

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManagerImpl @Inject constructor(
    private val auth: FirebaseAuth
) : SessionManager {

    override fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User not logged in")
    }

    override fun currentUserId(): String? {
        return auth.currentUser?.uid
    }
}