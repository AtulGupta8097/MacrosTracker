package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken)
}
