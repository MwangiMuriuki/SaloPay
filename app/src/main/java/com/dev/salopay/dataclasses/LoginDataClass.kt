package com.dev.salopay.dataclasses

data class LoginDataClass(
    var success: Boolean = false,
    var message: String? = null,
    var data: ResponseData? = null
) {
    data class ResponseData(
        var token: String? = null,
        var user: UserDataClass? = null
    ){
        data class UserDataClass(
            var id: Int = 0,
            var user_name: String? = null,
            var user_info: UserInfo? = null
        ){
            data class UserInfo(
                var id: Int = 0,
                var first_name: String? = null,
                var last_name: String? = null,
                var phone_number: String? = null,
                var id_number: String? = null,
                var sa_amount_limit: Int = 0,
                var client_name: String? = null,
                var sa_amount_limit_formatted: String? = null
            ){}
        }
    }
}