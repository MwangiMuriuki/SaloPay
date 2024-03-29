package com.dev.salopay.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.dev.salopay.R
import com.dev.salopay.adapters.AdapterCurrentAdvanceTransactions
import com.dev.salopay.databinding.ActivityMainBinding
import com.dev.salopay.dataclasses.CurrentAdvanceTransactionsDataClass
import com.dev.salopay.dataclasses.ServiceData
import com.dev.salopay.dataclasses.ServiceDataClass
import com.dev.salopay.utils.BaseActivity
import com.dev.salopay.utils.Config
import com.dev.salopay.utils.PreferenceManager
import java.io.Serializable
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var preferenceManager: PreferenceManager
    var interestRate: Int = 0
    var transactionCharges: List<ServiceData.TransactionCharges?> = ArrayList<ServiceData.TransactionCharges?>()
    var transactionsDataClass: List<CurrentAdvanceTransactionsDataClass.TransactionData.TransactionsList?> = ArrayList<CurrentAdvanceTransactionsDataClass.TransactionData.TransactionsList?>()
    var cycle_id: Int = 0
    var cycleIsActive: Boolean = true
    var hasActiveCycle: Boolean = true
    var advanceLimit: Int? = 0
    var advanceLimitFormatted: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        preferenceManager = PreferenceManager(this)
        getCurrentTime()
        fetchServiceData()
        fetchCurrentAdvanceData()

        binding.username.text = preferenceManager.firstName

        binding.currentAdvanceRV.layoutManager = LinearLayoutManager(this)

        binding.logoutView.setOnClickListener {
            showConfirmAlertDialog()
        }
    }

    private fun getCurrentTime() {
        val currentTime = Calendar.getInstance()
        Log.d("time", currentTime.toString())

        val now = Calendar.getInstance()
        if (currentTime[Calendar.AM_PM] == Calendar.AM) {
            // AM
            Log.d("LoggedTime: ", currentTime[Calendar.HOUR].toString() + " AM")
            binding.timeGreeting.text = "Good Morning"
        } else {
            // PM
            Log.d("LoggedTime: ", currentTime[Calendar.HOUR].toString() + " PM")
            if (currentTime[Calendar.HOUR] >= 5){
                binding.timeGreeting.text = "Good Evening"
            }
            else{
                binding.timeGreeting.text = "Good Afternoon"
            }
        }
    }

    private fun showConfirmAlertDialog() {
        val customView: View = layoutInflater.inflate(R.layout.confirm_action_alert, null)
        val messageField = customView.findViewById<TextView>(R.id.message)
        val confirmButton = customView.findViewById<Button>(R.id.btnProceed)
        val cancelButton = customView.findViewById<Button>(R.id.btnCancel)

        messageField.text = "You are about to Logout of your Account. Are you sure you want to perform this action?"
        confirmButton.text = "Logout"

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setView(customView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        cancelButton.setOnClickListener { dialog.dismiss() }

        confirmButton.setOnClickListener {
            preferenceManager.apiKey = ""

            val intent: Intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchCurrentAdvanceData() {
        val FULL_PATH = Config.BASE_URL + Config.advanceTransaction
        val apiToken = preferenceManager.apiKey

        AndroidNetworking.post(FULL_PATH)
            .addQueryParameter("token", apiToken)
            .addBodyParameter("service_type", "SALOPAY_ADVANCE")
            .setTag("FetchService")
            .setPriority(Priority.HIGH)
            .build()
            .getAsObject(CurrentAdvanceTransactionsDataClass::class.java, object : ParsedRequestListener<CurrentAdvanceTransactionsDataClass>{
                override fun onResponse(response: CurrentAdvanceTransactionsDataClass?) {
                    binding.progressBar.visibility = View.GONE
                    if (response!=null){
                        val success = response.success
                        val message = response.message

                        if (success){
                            transactionsDataClass = response.data?.transactions as ArrayList<CurrentAdvanceTransactionsDataClass.TransactionData.TransactionsList?>

                            if (!transactionsDataClass.isEmpty()){
                                binding.transactionPlaceholder.visibility = View.GONE
                                binding.currentAdvanceRV.visibility = View.VISIBLE
                                binding.currentAdvanceRV.adapter = AdapterCurrentAdvanceTransactions(this@MainActivity, transactionsDataClass)
                            }
                            else{
                                binding.transactionPlaceholder.visibility = View.VISIBLE
                                binding.currentAdvanceRV.visibility = View.GONE
                            }
                        }
                        else{
                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()

                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("anError", anError?.message.toString())
                }
            })
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
            .getAsObject( ServiceDataClass::class.java, object : ParsedRequestListener<ServiceDataClass> {
                override fun onResponse(response: ServiceDataClass?) {
                    if (response!=null) {
                        val success = response.success
                        val message = response.message
                        val fetchedServiceData = response.data?.service

                        if (success){
                            Log.d("success_message", message!!)
                            //Toast.makeText(this@MainActivity, "Success: " + message, Toast.LENGTH_LONG).show()

                            if (fetchedServiceData != null){
                                cycle_id = fetchedServiceData.active_cycle?.id!!
                                transactionCharges = response.data!!.transaction_charges!!
                                hasActiveCycle = fetchedServiceData.has_active_cycle
                                interestRate = fetchedServiceData.active_cycle?.rate!!
                                cycleIsActive = fetchedServiceData.active_cycle?.is_open!!
//                                cycleIsActive = false

                                preferenceManager.advanceLimit = response.data?.request_limit?.amount.toString()
                                preferenceManager.advanceLimitFormatted = response.data?.request_limit?.amount_formatted

                                val advanceLimit = preferenceManager.advanceLimitFormatted
                                binding.advanceLimit.text = advanceLimit
                                binding.previousAdvance.text = response.data?.previous_transaction

                                if (hasActiveCycle){
                                    if (cycleIsActive){
                                        binding.requestAdvanceBtn.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.accent_btn_background)
                                        binding.requestAdvanceBtn.setOnClickListener {
                                            redirect()
                                        }
                                    }
                                    else{
                                        binding.requestAdvanceBtn.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.btn_grey_background)
                                        binding.requestAdvanceBtn.setOnClickListener {
                                            Toast.makeText(this@MainActivity, "The Advance Period for this month has been Closed. Please wait until the next period.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(this@MainActivity, "You do not have an active cycle. Please contact your HR", Toast.LENGTH_LONG).show()
                                }

                            }
                        }
                        else{
                            Toast.makeText(this@MainActivity, "Error Fetching Service: " + message, Toast.LENGTH_LONG).show()
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

    private fun redirect() {
        val intent: Intent = Intent(this, ActivityRequestAdvance::class.java)
        val bundle = Bundle()
        bundle.putSerializable("transaction_charges", transactionCharges as Serializable)

        intent.putExtra("interestRate", interestRate.toString())
        intent.putExtra("cycleID", cycle_id.toString())
        intent.putExtras(bundle)

        startActivity(intent)
    }

}