package com.dev.salopay.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivitySplashScreenBinding

class ActivitySplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        binding.icon.animate().alpha(1f).setStartDelay(100).setDuration(1500)

        val SPLASH_TIME_OUT = 4000
        Handler().postDelayed({

            val intent = Intent(this@ActivitySplashScreen, ActivityCheckLogin::class.java)
            startActivity(intent)
            finish()

        }, SPLASH_TIME_OUT.toLong())
    }
}