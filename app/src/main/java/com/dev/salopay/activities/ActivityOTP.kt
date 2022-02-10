package com.dev.salopay.activities

import android.app.AlertDialog
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
import com.dev.salopay.databinding.ActivityOtpBinding
import com.dev.salopay.dataclasses.AccountVerificationResponseDataClass
import com.dev.salopay.dataclasses.VerifyOtpDataClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import dmax.dialog.SpotsDialog

class ActivityOTP : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    lateinit var preferenceManager: PreferenceManager
    var alertDialog: AlertDialog? = null
    var phoneNumber: String? = null
    var otp: String? = null
    var otpInt: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)
        AndroidNetworking.initialize(this)
        preferenceManager = PreferenceManager(this)
        alertDialog = SpotsDialog(this, R.style.verifyAccAlert)

        binding.mainLayout.bringToFront()

        phoneNumber = intent.getStringExtra("phoneNumber")
        binding.btnNext.setOnClickListener {
            otp = binding.otpView.text.toString()
            otpInt = otp!!.toInt()

            if (otp.isNullOrEmpty()){
                Toast.makeText(this, otp, Toast.LENGTH_LONG).show()
            }
            else{
                (alertDialog as SpotsDialog).setCancelable(false)
                (alertDialog as SpotsDialog).show()
                verifyOTP()
            }
        }
    }

    private fun verifyOTP(){
        val FULL_PATH = Config.BASE_URL + Config.verify_otp

        AndroidNetworking.post(FULL_PATH)
            .addBodyParameter("phone_number", phoneNumber)
            .addBodyParameter("otp_code", otp)
            .setTag("FetchAccounts")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( VerifyOtpDataClass::class.java, object : ParsedRequestListener<VerifyOtpDataClass> {
                override fun onResponse(response: VerifyOtpDataClass?) {
                    alertDialog?.dismiss()
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        if (success){
                            Log.d("success_message", message!!)
                            Toast.makeText(this@ActivityOTP, "Success: " + message, Toast.LENGTH_LONG).show()

                            redirect()
                        }
                        else{
                            Toast.makeText(this@ActivityOTP, "Failed: " + message, Toast.LENGTH_LONG).show()

                            Log.d("failure_message", message!!)
                        }
                    }
                    else{
                        Toast.makeText(this@ActivityOTP, "Response is null", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(anError: ANError?) {
                    alertDialog?.dismiss()

                    Log.e("anError", anError?.message.toString())
                }
            })
    }

    private fun redirect() {
        val intent: Intent = Intent(this, ActivitySetPassword::class.java)
        startActivity(intent)
        finish()
    }
}