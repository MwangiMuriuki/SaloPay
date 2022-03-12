package com.dev.salopay.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityMainBinding
import com.dev.salopay.dataclasses.LoginDataClass
import com.dev.salopay.dataclasses.ServiceDataClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceManager = PreferenceManager(this)
        fetchServiceData()

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

    private fun fetchServiceData() {
        val FULL_PATH = Config.BASE_URL + Config.serviceData
        val apiToken = preferenceManager.apiKey

        AndroidNetworking.post(FULL_PATH)
            .addQueryParameter("token", apiToken)
            .addBodyParameter("service_type", "SALOPAY_ADVANCE")
            .setTag("FetchService")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( ServiceDataClass::class.java, object :
                ParsedRequestListener<ServiceDataClass> {
                override fun onResponse(response: ServiceDataClass?) {
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        val fecthedServiceData = response.data?.service

                        if (success){
                            Log.d("success_message", message!!)
                            Toast.makeText(this@MainActivity, "Success: " + message, Toast.LENGTH_LONG).show()

                            if (fecthedServiceData != null){
                                val cycle_id = fecthedServiceData.active_cycle?.id
                                preferenceManager.cycleId = cycle_id.toString()
                            }

                        }
                        else{
                            Toast.makeText(this@MainActivity, "Failed: " + message, Toast.LENGTH_LONG).show()
                            Log.d("failure_message", message!!)
                        }
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Response is null", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("anError", anError?.message.toString())
                }
            })
    }

}