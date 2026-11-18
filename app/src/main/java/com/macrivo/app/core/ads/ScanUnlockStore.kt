package com.macrivo.app.core.ads

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScanUnlockStore @Inject constructor() {

    private val _isUnlocked = MutableStateFlow(false)
    val isUnlocked: StateFlow<Boolean> = _isUnlocked.asStateFlow()

    /** One credit only: granting while a credit is already there does not stack. */
    fun grant() {
        _isUnlocked.value = true
    }

    fun tryConsume(): Boolean = _isUnlocked.compareAndSet(expect = true, update = false)
}
