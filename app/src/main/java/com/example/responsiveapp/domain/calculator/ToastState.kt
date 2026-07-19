package com.example.responsiveapp.domain.calculator

import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast

data class ToastState(
    val visible: Boolean = false,
    val message: String = "",
    val type: CustomToastProperty = SuccessToast(),
    val duration: Long = 3000L
)