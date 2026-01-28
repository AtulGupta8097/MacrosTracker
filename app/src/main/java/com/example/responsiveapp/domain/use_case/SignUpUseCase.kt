package com.example.responsiveapp.domain.use_case

import com.example.responsiveapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = authRepository.signUp(email, password)
}