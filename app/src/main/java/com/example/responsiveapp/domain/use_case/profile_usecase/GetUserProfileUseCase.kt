package com.example.responsiveapp.domain.use_case.profile_usecase

import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository

class GetUserProfileUseCase(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): UserProfile? {
        return repository.getUserProfile()
    }
}
