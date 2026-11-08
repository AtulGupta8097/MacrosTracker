package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.repository.UserProfileRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CompleteSignInUseCase @Inject constructor(
    private val appPrefManager: AppPrefManager,
    private val restoreUserDataUseCase: RestoreUserDataUseCase,
    private val userProfileRepository: UserProfileRepository,
) {
    suspend operator fun invoke(): Boolean {
        appPrefManager.setLoggedIn(true)
        restoreUserDataUseCase()

        val hasProfile = try {
            userProfileRepository.getUserProfile() != null
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            false
        }
        if (hasProfile) appPrefManager.setUserSetup(true)

        return appPrefManager.isUserSetup.first()
    }
}
