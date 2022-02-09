package com.dev.salopay.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityGetStartedBinding
import com.dev.salopay.dataclasses.AccountVerificationResponseDataClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import dmax.dialog.SpotsDialog

class ActivityGetStarted : AppCompatActivity() {
    private lateinit var binding: ActivityGetStartedBinding
    lateinit var preferenceManager: PreferenceManager
    var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_started)
        AndroidNetworking.initialize(this)
        preferenceManager = PreferenceManager(this)
        alertDialog = SpotsDialog(this, R.style.verifyAccAlert)

        binding.mainLayout.bringToFront()
        binding.toLoginLayout.bringToFront()

        binding.btnNext.setOnClickListener {

            validateFields()

        }

        binding.toLoginLayout.setOnClickListener {
            var intent: Intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
        }
    }

    private fun validateFields() {
        var companyCode = binding.companyCodeField.text
        var nationalID = binding.idNumberField.text

        if (companyCode.isEmpty()){

        }
        else if (nationalID.isEmpty()){

        }
        else{

            (alertDialog as SpotsDialog).setCancelable(false)
            (alertDialog as SpotsDialog).show()
            verifyUser(companyCode, nationalID)


        }
    }

    private fun verifyUser(companyCode: Editable, nationalID: Editable) {
        val FULL_PATH = Config.BASE_URL + Config.verify_account
        val auth_token: String? = preferenceManager.apiKey

        AndroidNetworking.post(FULL_PATH)
            .addBodyParameter("code", companyCode.toString())
            .addBodyParameter("id_number", nationalID.toString())
            .setTag("FetchAccounts")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( AccountVerificationResponseDataClass::class.java,
                object : ParsedRequestListener<AccountVerificationResponseDataClass> {
                    override fun onResponse(response: AccountVerificationResponseDataClass?) {
                        alertDialog?.dismiss()
                        if (response!=null) {
                            val success = response.success
                            val message = response.message
                            if (success){
                                Log.d("success_message", message!!)
                                redirect()
                            }
                            else{
                                Log.d("failure_message", message!!)
                            }
                        }
                        else{

                        }


                    }

                    override fun onError(anError: ANError?) {
                        alertDialog?.dismiss()

                        Log.e("anError", anError?.message.toString())
                    }
                })
    }

    private fun redirect(){
        var intent: Intent = Intent(this, ActivityOTP::class.java)
        startActivity(intent)
    }
}