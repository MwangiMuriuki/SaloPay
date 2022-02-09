package com.dev.salopay.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityOtpBinding

class ActivityOTP : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)

        binding.mainLayout.bringToFront()

        binding.btnNext.setOnClickListener {
            var intent: Intent = Intent(this, ActivitySetPassword::class.java)
            startActivity(intent)
        }
    }
}