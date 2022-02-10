package com.dev.salopay.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityMainBinding
import com.dev.salopay.utils.PreferenceManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceManager = PreferenceManager(this)

        var advanceLimit = preferenceManager.advanceLimit

        binding.advanceLimit.text = "KES " + advanceLimit
        binding.username.text = preferenceManager.firstName

        binding.logoutView.setOnClickListener {
            preferenceManager.apiKey = ""

            val intent: Intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }

    }
}