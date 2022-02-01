package com.dev.salopay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dev.salopay.databinding.ActivityGetStartedBinding

class ActivityGetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_started)

        binding.mainLayout.bringToFront()

        binding.btnNext.setOnClickListener {
            var intent: Intent = Intent(this, ActivityOTP::class.java)
            startActivity(intent)
        }
    }
}