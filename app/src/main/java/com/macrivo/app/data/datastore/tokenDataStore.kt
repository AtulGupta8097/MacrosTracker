
package com.macrivo.app.data.datastore

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.security.GeneralSecurityException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "fatsecret_token_prefs"
)

interface TokenDataStore {

    suspend fun saveToken(token: String, expiryTime: Long)

    suspend fun getToken(): String?

    suspend fun getTokenExpiry(): Long

    suspend fun clearToken()

    fun tokenFlow(): Flow<String?>

    fun tokenExpiryFlow(): Flow<Long>
}

@Singleton
class EncryptedTokenDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context
) : TokenDataStore {

    private val dataStore = context.tokenDataStore

    companion object {
        private val KEY_ACCESS_TOKEN_ENCRYPTED =
            stringPreferencesKey("access_token_enc")

        private val KEY_ACCESS_TOKEN_LEGACY =
            stringPreferencesKey("access_token")

        private val KEY_TOKEN_EXPIRY =
            longPreferencesKey("token_expiry")
    }

    override suspend fun saveToken(token: String, expiryTime: Long) {
        val encrypted = try {
            TokenCipher.encrypt(token)
        } catch (e: GeneralSecurityException) {
            clearToken()
            return
        }

        dataStore.edit { preferences ->
            preferences[KEY_ACCESS_TOKEN_ENCRYPTED] = encrypted
            preferences[KEY_TOKEN_EXPIRY] = expiryTime
            preferences.remove(KEY_ACCESS_TOKEN_LEGACY)
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data
            .map { preferences -> preferences.decryptedToken() }
            .first()
    }

    override suspend fun getTokenExpiry(): Long {
        return dataStore.data
            .map { preferences -> preferences[KEY_TOKEN_EXPIRY] ?: 0L }
            .first()
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(KEY_ACCESS_TOKEN_ENCRYPTED)
            preferences.remove(KEY_ACCESS_TOKEN_LEGACY)
            preferences.remove(KEY_TOKEN_EXPIRY)
        }
    }

    override fun tokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences.decryptedToken()
        }
    }

    override fun tokenExpiryFlow(): Flow<Long> {
        return dataStore.data.map { preferences ->
            preferences[KEY_TOKEN_EXPIRY] ?: 0L
        }
    }

    private fun Preferences.decryptedToken(): String? {
        return this[KEY_ACCESS_TOKEN_ENCRYPTED]?.let { TokenCipher.decrypt(it) }
    }
}

/**
 * AES-256-GCM encryption using a non-exportable key stored in Android Keystore.
 * Stored format: Base64(iv + ciphertext).
 */
private object TokenCipher {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "macrivo_fatsecret_token_key"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val GCM_TAG_BITS = 128
    private const val IV_BYTES = 12

    fun encrypt(plainText: String): String {
        return try {
            encryptWith(getOrCreateKey(), plainText)
        } catch (e: GeneralSecurityException) {
            deleteKey()
            encryptWith(getOrCreateKey(), plainText)
        }
    }

    fun decrypt(encoded: String): String? {
        return try {
            val bytes = Base64.decode(encoded, Base64.NO_WRAP)

            if (bytes.size <= IV_BYTES) {
                return null
            }

            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_BITS, bytes, 0, IV_BYTES)
            )

            String(
                cipher.doFinal(bytes, IV_BYTES, bytes.size - IV_BYTES),
                Charsets.UTF_8
            )
        } catch (e: GeneralSecurityException) {
            null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    private fun encryptWith(key: SecretKey, plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        cipher.init(
            Cipher.ENCRYPT_MODE,
            key
        )

        val cipherText = cipher.doFinal(
            plainText.toByteArray(Charsets.UTF_8)
        )

        return Base64.encodeToString(
            cipher.iv + cipherText,
            Base64.NO_WRAP
        )
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }

        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let {
            return it
        }

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        generator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
        )

        return generator.generateKey()
    }

    private fun deleteKey() {
        try {
            KeyStore.getInstance(ANDROID_KEYSTORE)
                .apply { load(null) }
                .deleteEntry(KEY_ALIAS)
        } catch (_: Exception) {
            // Ignore cleanup failures; the retry will report the encryption error.
        }
    }
}
