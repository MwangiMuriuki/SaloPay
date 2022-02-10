package com.dev.salopay.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivitySetPasswordBinding
import com.dev.salopay.dataclasses.SetPasswordResponseClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import dmax.dialog.SpotsDialog

class ActivitySetPassword : AppCompatActivity() {
    private lateinit var binding: ActivitySetPasswordBinding
    lateinit var preferenceManager: PreferenceManager
    var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_set_password)
        AndroidNetworking.initialize(this)
        preferenceManager = PreferenceManager(this)
        alertDialog = SpotsDialog(this, R.style.verifyAccAlert)

        binding.mainLayout.bringToFront()

        binding.btnNext.setOnClickListener {

            validateFields()

        }
    }

    private fun validateFields() {
        val password = binding.enterPasswordField.text.toString()
        val confirmPassword = binding.confirmPasswordField.text.toString()

        if (password.isEmpty()){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_LONG).show()
        }
        else if (confirmPassword.isEmpty()){
            Toast.makeText(this, "Please Confirm Your Password", Toast.LENGTH_LONG).show()
        }
        else if (confirmPassword != password){
            Toast.makeText(this, "Passwords do not match. please try again", Toast.LENGTH_LONG).show()
        }
        else {
            (alertDialog as SpotsDialog).setCancelable(false)
            (alertDialog as SpotsDialog).show()
            setPassword(password)
        }
    }


    private fun setPassword(password: String?) {
        val FULL_PATH = Config.BASE_URL + Config.create_password
        val userID = preferenceManager.userIdNumber

        AndroidNetworking.post(FULL_PATH)
            .addBodyParameter("id_number",userID )
            .addBodyParameter("password", password)
            .setTag("FetchAccounts")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( SetPasswordResponseClass::class.java, object : ParsedRequestListener<SetPasswordResponseClass> {
                override fun onResponse(response: SetPasswordResponseClass?) {
                    alertDialog?.dismiss()
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        if (success){
                            Log.d("success_message", message!!)
                            Toast.makeText(this@ActivitySetPassword, "Success: " + message, Toast.LENGTH_LONG).show()

                            preferenceManager.firstName = response.data?.user?.user_info?.first_name
                            preferenceManager.lastName = response.data?.user?.user_info?.last_name
                            preferenceManager.advanceLimit = response.data?.user?.user_info?.sa_amount_limit.toString()
                            preferenceManager.companyName = response.data?.user?.user_info?.client_name

                            redirect()
                        }
                        else{
                            Toast.makeText(this@ActivitySetPassword, "Failed: " + message, Toast.LENGTH_LONG).show()

                            Log.d("failure_message", message!!)
                        }
                    }
                    else{
                        Toast.makeText(this@ActivitySetPassword, "Response is null", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onError(anError: ANError?) {
                    alertDialog?.dismiss()

                    Log.e("anError", anError?.message.toString())
                }
            })
    }

    private fun redirect() {
        val intent: Intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }
}