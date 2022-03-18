package com.dev.salopay.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

    var companyCode: String? = null
    var nationalID: String? = null

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

        binding.getCompanyCode.setOnClickListener {
                val customView: View = layoutInflater.inflate(R.layout.get_company_code_alert, null)
                val confirmButton = customView.findViewById<Button>(R.id.btnProceed)

                val builder = AlertDialog.Builder(this@ActivityGetStarted)
                builder.setView(customView)
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()

                confirmButton.setOnClickListener {
                    dialog.dismiss()
                }

        }
    }

    private fun validateFields() {
        companyCode = binding.companyCodeField.text.toString()
        nationalID = binding.idNumberField.text.toString()

        if (companyCode!!.isEmpty()){
            Toast.makeText(this, "Please Enter your Company Code", Toast.LENGTH_LONG).show()
        }
        else if (nationalID!!.isEmpty()){
            Toast.makeText(this, "Please Enter your National ID Number", Toast.LENGTH_LONG).show()

        }
        else{

            (alertDialog as SpotsDialog).setCancelable(false)
            (alertDialog as SpotsDialog).show()

            verifyUser(companyCode, nationalID)

        }
    }

    private fun verifyUser(companyCode: String?, nationalID: String?) {
        val FULL_PATH = Config.BASE_URL + Config.verify_account

        AndroidNetworking.post(FULL_PATH)
            .addBodyParameter("code", companyCode.toString())
            .addBodyParameter("id_number", nationalID.toString())
            .setTag("FetchAccounts")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( AccountVerificationResponseDataClass::class.java, object : ParsedRequestListener<AccountVerificationResponseDataClass> {
                    override fun onResponse(response: AccountVerificationResponseDataClass?) {
                        alertDialog?.dismiss()
                        if (response!=null) {
                            val success = response.success
                            val message = response.message
                            if (success){
                                Log.d("success_message", message!!)
                                Toast.makeText(this@ActivityGetStarted, "Success: " + message, Toast.LENGTH_LONG).show()

                                val phoneNumber = response.data?.phone_number

                                redirect(phoneNumber)
                            }
                            else{
                                Toast.makeText(this@ActivityGetStarted, "Failed: " + message, Toast.LENGTH_LONG).show()

                                Log.d("failure_message", message!!)
                            }
                        }
                        else{
                            Toast.makeText(this@ActivityGetStarted, "Response is null", Toast.LENGTH_LONG).show()
                        }

                    }

                    override fun onError(anError: ANError?) {
                        alertDialog?.dismiss()

                        Log.e("anError", anError?.message.toString())
                    }
                })
    }

    private fun redirect(phoneNumber: String?) {
        preferenceManager.userIdNumber = nationalID
        val intent: Intent = Intent(this, ActivityOTP::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        startActivity(intent)
        finish()
    }
}