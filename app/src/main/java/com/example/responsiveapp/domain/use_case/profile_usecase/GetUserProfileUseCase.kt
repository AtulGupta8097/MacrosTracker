package com.example.responsiveapp.domain.use_case.profile_usecase

import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): UserProfile? {
        return repository.getUserProfile()
    }
}
