package com.macrivo.app.domain.use_case.authentication

import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.domain.repository.UserProfileRepository
import com.macrivo.app.domain.session.SessionManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CompleteSignInUseCase @Inject constructor(
    private val appPrefManager: AppPrefManager,
    private val restoreUserDataUseCase: RestoreUserDataUseCase,
    private val userProfileRepository: UserProfileRepository,
    private val sessionManager: SessionManager,
    private val claimLocalData: ClaimLocalDataUseCase,
) {
    suspend operator fun invoke(): Boolean {
        claimLocalData(sessionManager.requireUserId())
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
