package com.dev.salopay.utils

class Config {
    companion object{
        val GLOBAL_SHARED_PREF_KEY = "salopay_shared_prefs"
        val BASE_URL: String = "https://salopay.mobitek-solutions.com/api/app/"
        val login_url: String = "authenticate_user"
        var verify_account: String = "verify_account"
        var verify_otp: String = "verify_otp_code"
        var create_password: String = "create_user_account"
    }
}