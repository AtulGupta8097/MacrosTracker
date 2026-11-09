package com.macrivo.app.domain.use_case.profile

import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.domain.repository.UserProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): UserProfile? {
        return repository.getUserProfile()
    }
}
