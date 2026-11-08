package com.example.responsiveapp.presentation.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CancellationException

sealed interface GoogleTokenResult {
    data class Success(val idToken: String) : GoogleTokenResult
    data object Cancelled : GoogleTokenResult
    data object NoAccount : GoogleTokenResult
    data object NotConfigured : GoogleTokenResult
    data object Failed : GoogleTokenResult
}

class GoogleIdTokenProvider(private val context: Context) {

    suspend fun getIdToken(): GoogleTokenResult {
        val resId = context.resources.getIdentifier(
            "default_web_client_id", "string", context.packageName
        )
        if (resId == 0) return GoogleTokenResult.NotConfigured

        val option = GetSignInWithGoogleOption.Builder(context.getString(resId)).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()

        return try {
            val credential = CredentialManager.create(context)
                .getCredential(context, request)
                .credential

            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                GoogleTokenResult.Success(
                    GoogleIdTokenCredential.createFrom(credential.data).idToken
                )
            } else {
                GoogleTokenResult.Failed
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: GetCredentialCancellationException) {
            GoogleTokenResult.Cancelled
        } catch (e: NoCredentialException) {
            GoogleTokenResult.NoAccount
        } catch (e: GetCredentialException) {
            GoogleTokenResult.Failed
        } catch (e: GoogleIdTokenParsingException) {
            GoogleTokenResult.Failed
        }
    }
}

@Composable
fun rememberGoogleIdTokenProvider(): GoogleIdTokenProvider {
    val context = LocalContext.current
    return remember(context) { GoogleIdTokenProvider(context) }
}

fun GoogleTokenResult.failureMessage(): String? = when (this) {
    is GoogleTokenResult.Success -> null
    GoogleTokenResult.Cancelled -> null // user backed out: stay quiet
    GoogleTokenResult.NoAccount ->
        "No Google account found on this device. Add one in your device settings and try again."
    GoogleTokenResult.NotConfigured -> "Google sign-in isn't set up for this build yet."
    GoogleTokenResult.Failed -> "Google sign-in didn't complete. Please try again."
}
