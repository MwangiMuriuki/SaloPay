package com.dev.salopay.dataclasses

data class ServiceRequestDataClass (
    var success: Boolean = false,
    var message: String? = null,
    var data: String? = null
){}