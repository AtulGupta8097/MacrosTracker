package com.macrivo.app.domain.use_case.profile

import com.macrivo.app.domain.model.UserProfile
import com.macrivo.app.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository,
) {
    operator fun invoke(): Flow<UserProfile?> = repository.observeUserProfile()
}
