package com.dev.salopay.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityRequestAdvanceBinding
import com.dev.salopay.dataclasses.ServiceRequestDataClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import java.math.RoundingMode

class ActivityRequestAdvance : AppCompatActivity() {
    private lateinit var binding: ActivityRequestAdvanceBinding
    lateinit var preferenceManager: PreferenceManager
    var intRate: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_advance)
        preferenceManager = PreferenceManager(this)

        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.username.text = preferenceManager.firstName
        binding.advanceLimit.text = preferenceManager.advanceLimitFormatted

        intRate = intent.getStringExtra("interestRate")?.toInt()!!

        binding.advanceField.addTextChangedListener(textWatcher)

        binding.requestAdvanceBtn.setOnClickListener {
            validateField()
        }
    }

    fun validateField(){
        var amountRequested: String = binding.advanceField.text.toString()
        var userAdvanceLimit = preferenceManager.advanceLimit

        if (amountRequested.isNullOrEmpty()){
            Toast.makeText(this@ActivityRequestAdvance, "Please Enter an Amount to Request", Toast.LENGTH_LONG).show()
        }
        else if (amountRequested > userAdvanceLimit){
            Toast.makeText(this@ActivityRequestAdvance, "Amount Entered is More Than Your Allowed Limit", Toast.LENGTH_LONG).show()
        }
        else{
            makeAdvanceRequest(amountRequested)
        }
    }

    val textWatcher  = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(enteredText: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.requestedAmount.text = enteredText

            if (!enteredText.isNullOrEmpty()){
                calculateDeductions(enteredText)
            }

        }

        override fun afterTextChanged(enteredText: Editable?) {
           // calculateDeductions(enteredText)
        }
    }

    private fun calculateDeductions(enteredText: CharSequence?) {
        var passedText = enteredText
      //  Toast.makeText(this@ActivityRequestAdvance, "Success: " + passedText, Toast.LENGTH_SHORT).show()

//        val interest = principle.toBigDecimal()
//            .multiply(rate?.toBigDecimal()?.divide(100.toBigDecimal(), 2, RoundingMode.HALF_EVEN))
//            .multiply(time.toBigDecimal().divide(12.toBigDecimal(), 2, RoundingMode.HALF_EVEN))

//        var intCharged = enteredText.toString().toBigDecimal()
//            .multiply(intRate.toBigDecimal().divide(100.toBigDecimal(), 2, RoundingMode.HALF_EVEN))

        val intCharged = enteredText.toString().toBigDecimal()
            .multiply(intRate.toBigDecimal().divide(100.toBigDecimal(), 2, RoundingMode.HALF_EVEN))



        Toast.makeText(this@ActivityRequestAdvance, "Success: " + intCharged, Toast.LENGTH_SHORT).show()

        binding.interestCharged.text = intCharged.toString()

    }

    private fun makeAdvanceRequest(amountRequested: String) {
        val FULL_PATH = Config.BASE_URL + Config.requestAdvance
        val apiToken = preferenceManager.apiKey

        AndroidNetworking.post(FULL_PATH)
            .addQueryParameter("token", apiToken)
            .addBodyParameter("cycle_id", 6.toString())
            .addBodyParameter("amount", amountRequested)
            .setTag("RequestAdvance")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( ServiceRequestDataClass::class.java, object :
                ParsedRequestListener<ServiceRequestDataClass> {
                override fun onResponse(response: ServiceRequestDataClass?) {
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        val data = response.data

                        if (success){
                            Log.d("success_message", message!!)
                            Toast.makeText(this@ActivityRequestAdvance, "Success: " + message, Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this@ActivityRequestAdvance, "Failed: " + message, Toast.LENGTH_LONG).show()
                            Log.d("failure_message", message!!)
                        }
                    }
                    else{
                        Toast.makeText(this@ActivityRequestAdvance, "Response is null", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.e("anError", anError?.message.toString())
                }
            })
    }

}