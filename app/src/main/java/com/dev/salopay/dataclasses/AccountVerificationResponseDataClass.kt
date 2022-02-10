package com.dev.salopay.dataclasses

data class AccountVerificationResponseDataClass(
    var success: Boolean = false,
    var message: String? = null,
    var data: AccountData? = null
) {
    data class AccountData(
        var phone_number: String? = null
    ){}
}