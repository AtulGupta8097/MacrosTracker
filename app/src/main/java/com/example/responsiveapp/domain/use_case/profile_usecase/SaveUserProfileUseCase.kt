package com.example.responsiveapp.domain.use_case.profile_usecase

import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(profile: UserProfile) : Result<Unit> {
        return repository.saveUserProfile(profile)
    }
}
