package com.example.responsiveapp.domain.use_case.profile

import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository,
) {
    operator fun invoke(): Flow<UserProfile?> = repository.observeUserProfile()
}
