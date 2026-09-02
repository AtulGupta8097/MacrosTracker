package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String) =
        authRepository.sendPasswordResetEmail(email)
}
