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
import com.dev.salopay.databinding.ActivityLoginBinding
import com.dev.salopay.dataclasses.LoginDataClass
import com.dev.salopay.dataclasses.SetPasswordResponseClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import dmax.dialog.SpotsDialog

class ActivityLogin : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var preferenceManager: PreferenceManager
    var alertDialog: AlertDialog? = null
    var password: String? = null
    var nationalID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        AndroidNetworking.initialize(this)
        preferenceManager = PreferenceManager(this)
        alertDialog = SpotsDialog(this, R.style.verifyAccAlert)

        binding.mainLayout.bringToFront()

        binding.btnNext.setOnClickListener {
            validateFields()
        }

        binding.toGetStartedLayout.setOnClickListener {
            val intent: Intent = Intent(this, ActivityGetStarted::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateFields() {
        password = binding.passwordField.text.toString()
        nationalID = binding.idField.text.toString()

        if (nationalID!!.isEmpty()){
            Toast.makeText(this, "Please Enter your National ID Number", Toast.LENGTH_LONG).show()
        }
        else if (password!!.isEmpty()){
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_LONG).show()
        }
        else{

            (alertDialog as SpotsDialog).setCancelable(false)
            (alertDialog as SpotsDialog).show()

            login(password, nationalID!!)

        }
    }

    private fun login(password: String?, nationalID: String) {
        val FULL_PATH = Config.BASE_URL + Config.login_url

        AndroidNetworking.post(FULL_PATH)
            .addBodyParameter("user_name", this.nationalID)
            .addBodyParameter("password", password)
            .setTag("FetchAccounts")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( LoginDataClass::class.java, object : ParsedRequestListener<LoginDataClass> {
                override fun onResponse(response: LoginDataClass?) {
                    alertDialog?.dismiss()
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        if (success){
                            Log.d("success_message", message!!)
                            Toast.makeText(this@ActivityLogin, "Success: " + message, Toast.LENGTH_LONG).show()

                            preferenceManager.apiKey = response.data?.token
                            preferenceManager.firstName = response.data?.user?.user_info?.first_name
                            preferenceManager.lastName = response.data?.user?.user_info?.last_name
                            preferenceManager.advanceLimit = response.data?.user?.user_info?.sa_amount_limit.toString()
                            preferenceManager.advanceLimitFormatted = response.data?.user?.user_info?.sa_amount_limit_formatted

                            redirect()
                        }
                        else{
                            Toast.makeText(this@ActivityLogin, "Failed: " + message, Toast.LENGTH_LONG).show()

                            Log.d("failure_message", message!!)
                        }
                    }
                    else{
                        Toast.makeText(this@ActivityLogin, "Response is null", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    alertDialog?.dismiss()

                    Log.e("anError", anError?.message.toString())
                }
            })
    }

    private fun redirect() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}