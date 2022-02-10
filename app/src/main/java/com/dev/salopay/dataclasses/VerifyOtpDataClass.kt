package com.dev.salopay.dataclasses

data class VerifyOtpDataClass(
    var success: Boolean = false,
    var message: String? = null,
    var data: String? = null
) {
}