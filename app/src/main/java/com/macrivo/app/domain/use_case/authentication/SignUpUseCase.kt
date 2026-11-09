package com.macrivo.app.domain.use_case.authentication

import com.macrivo.app.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, name: String) = authRepository.signUp(email,password,name)
}