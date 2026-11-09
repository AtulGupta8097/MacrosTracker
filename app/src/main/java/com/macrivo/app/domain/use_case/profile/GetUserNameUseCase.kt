package com.macrivo.app.domain.use_case.profile

import com.macrivo.app.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getUserName()
    }
}
