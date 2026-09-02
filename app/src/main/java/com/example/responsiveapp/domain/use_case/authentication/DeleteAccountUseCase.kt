package com.example.responsiveapp.domain.use_case.authentication

import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.data.local.database.MacrosTrackerDatabase
import com.example.responsiveapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DeleteAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val database: MacrosTrackerDatabase,
    private val appPrefManager: AppPrefManager,
) {
    suspend operator fun invoke(): Flow<Result<Unit>> =
        authRepository.deleteAccount().map { result ->
            if (result.isSuccess) {
                withContext(Dispatchers.IO) {
                    database.clearAllTables()
                }
                appPrefManager.setLoggedIn(false)
                appPrefManager.setUserSetup(false)
            }
            result
        }
}
