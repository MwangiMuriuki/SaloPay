package com.dev.salopay.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.databinding.ActivityRequestAdvanceBinding
import com.dev.salopay.dataclasses.ServiceData
import com.dev.salopay.dataclasses.ServiceRequestDataClass
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import dmax.dialog.SpotsDialog
import java.math.RoundingMode

class ActivityRequestAdvance : AppCompatActivity() {
    private lateinit var binding: ActivityRequestAdvanceBinding
    lateinit var preferenceManager: PreferenceManager
    var alertDialog: AlertDialog? = null
    var intRate: Int = 0
    var cycleID: String? = null
    lateinit var transactionCharges: List<ServiceData.TransactionCharges?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_advance)
        preferenceManager = PreferenceManager(this)
        alertDialog = SpotsDialog(this, R.style.neutralAlert)


        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.username.text = preferenceManager.firstName
        binding.advanceLimit.text = preferenceManager.advanceLimitFormatted

        binding.preloaderLayout.bringToFront()
        intRate = intent.getStringExtra("interestRate")?.toInt()!!
        cycleID = intent.getStringExtra("cycleID")
        val myBundle = intent.extras
        transactionCharges = myBundle!!.getSerializable("transaction_charges") as List<ServiceData.TransactionCharges>

        binding.advanceField.addTextChangedListener(textWatcher)

        binding.requestAdvanceBtn.setOnClickListener {
            validateField()
        }
    }

    fun showLoader(){
        //binding.preloaderLayout.visibility = View.VISIBLE
        (alertDialog as SpotsDialog).setCancelable(false)
        (alertDialog as SpotsDialog).show()
    }
    fun hideLoader(){

        (alertDialog as SpotsDialog).hide()

        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun validateField(){
        var amountRequested: String = binding.advanceField.text.toString()
        var userAdvanceLimit: Int = preferenceManager.advanceLimit.toInt()

        if (amountRequested.isEmpty()){
            Toast.makeText(this@ActivityRequestAdvance, "Please Enter an Amount to Request", Toast.LENGTH_LONG).show()
        }
        else if (amountRequested.toInt() > userAdvanceLimit){
            Toast.makeText(this@ActivityRequestAdvance, "Amount Entered is More Than Your Allowed Limit", Toast.LENGTH_LONG).show()
        }
        else{
            showConfirmAlertDialog(amountRequested)
        }
    }

    private fun showConfirmAlertDialog(amountRequested: String) {
        val customView: View = layoutInflater.inflate(R.layout.confirm_action_alert, null)
        val messageField = customView.findViewById<TextView>(R.id.message)
        val confirmButton = customView.findViewById<Button>(R.id.btnProceed)
        val cancelButton = customView.findViewById<Button>(R.id.btnCancel)

        messageField.text = "Are you sure you want to proceed with this advance request of KES " + amountRequested + "?"

        val builder = AlertDialog.Builder(this@ActivityRequestAdvance)
        builder.setView(customView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        cancelButton.setOnClickListener { dialog.dismiss() }

        confirmButton.setOnClickListener {
            makeAdvanceRequest(amountRequested)
            dialog.dismiss()
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
        var tCharge: Int = 0

        val intCharged = enteredText.toString().toBigDecimal()
            .multiply(intRate.toBigDecimal().divide(100.toBigDecimal(), 2, RoundingMode.HALF_EVEN))

        if (enteredText.toString().toInt() <= 999){
            tCharge = transactionCharges[0]?.charges!!
            binding.transactionCharges.text = "-" + tCharge.toString()
        }
        else{
            tCharge = transactionCharges[1]?.charges!!
            binding.transactionCharges.text = "-" + tCharge.toString()
        }

        val finalAmount = enteredText.toString().toBigDecimal()
            .minus(tCharge.toBigDecimal())
            .minus(intCharged)

        binding.finalAmount.text = finalAmount.toString()

        binding.interestCharged.text =  "-" + intCharged.toString()

    }

    private fun makeAdvanceRequest(amountRequested: String) {
        showLoader()
        val FULL_PATH = Config.BASE_URL + Config.requestAdvance
        val apiToken = preferenceManager.apiKey

        AndroidNetworking.post(FULL_PATH)
            .addQueryParameter("token", apiToken)
            .addBodyParameter("cycle_id", cycleID)
            .addBodyParameter("amount", amountRequested)
            .setTag("RequestAdvance")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject( ServiceRequestDataClass::class.java, object :
                ParsedRequestListener<ServiceRequestDataClass> {
                override fun onResponse(response: ServiceRequestDataClass?) {
                    hideLoader()
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
                    hideLoader()
                    Toast.makeText(this@ActivityRequestAdvance, "Error Processing Request", Toast.LENGTH_LONG).show()

                    Log.e("anError", anError?.message.toString())
                }
            })
    }

}