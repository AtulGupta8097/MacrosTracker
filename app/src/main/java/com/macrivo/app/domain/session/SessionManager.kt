package com.macrivo.app.domain.session

interface SessionManager {

    fun requireUserId(): String

    fun currentUserId(): String?

    fun currentUserName(): String?

    fun currentUserEmail(): String?

    fun signOut()
}