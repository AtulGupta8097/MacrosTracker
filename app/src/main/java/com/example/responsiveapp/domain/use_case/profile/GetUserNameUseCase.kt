package com.example.responsiveapp.domain.use_case.profile

import com.example.responsiveapp.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetUserNameUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): String? {
        return repository.getUserName()
    }
}
