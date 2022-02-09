package com.dev.salopay.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivitySetPasswordBinding

class ActivitySetPassword : AppCompatActivity() {
    private lateinit var binding: ActivitySetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_password)

        binding.mainLayout.bringToFront()

        binding.btnNext.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}