package com.dev.salopay.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.salopay.utils.PreferenceManager

class ActivityCheckLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceManager = PreferenceManager(applicationContext)
        val auth_token = preferenceManager.apiKey

        if (auth_token!!.isEmpty()) {
            val intent = Intent(applicationContext, ActivityGetStarted::class.java)
            startActivity(intent)
            finish()
        }
        else{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}