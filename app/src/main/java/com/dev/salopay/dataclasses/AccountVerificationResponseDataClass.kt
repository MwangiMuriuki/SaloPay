package com.dev.salopay.dataclasses

data class AccountVerificationResponseDataClass(
    var success: Boolean = false,
    var message: String? = null,
    var data: String? = null
) {
}