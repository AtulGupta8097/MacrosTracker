package com.example.responsiveapp.domain.session

interface SessionManager {

    fun requireUserId(): String

    fun currentUserId(): String?
}