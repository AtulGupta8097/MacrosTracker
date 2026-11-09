package com.macrivo.app.domain.calculator

import com.macrivo.app.domain.model.CustomToastProperty
import com.macrivo.app.presentation.commoncomponent.SuccessToast

data class ToastState(
    val visible: Boolean = false,
    val message: String = "",
    val type: CustomToastProperty = SuccessToast(),
    val duration: Long = 3000L
)