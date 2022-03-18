package com.dev.salopay.utils

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.androidnetworking.AndroidNetworking
import com.dev.salopay.R
import com.dev.salopay.activities.ActivityLogin
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.system.exitProcess

open class BaseActivity : AppCompatActivity() {
    var handler: Handler? = null
    var runnable: Runnable? = null
    open var prefManager: PreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
        prefManager = PreferenceManager(applicationContext)
        AndroidNetworking.initialize(applicationContext)

        val auth_token =  prefManager?.apiKey
        //ToDO - Delete This Line
        Log.i("fetch_token", auth_token.toString())

        checkDeviceInactive()
    }

    private fun checkDeviceInactive() {

        runnable = Runnable {
            // Toast.makeText(applicationContext, "You have been inactive for the last 2 mins", Toast.LENGTH_LONG).show()
            Log.i("device_activity", "Device Inactive")

            prefManager?.apiKey = ""
            if (!this.isFinishing) {
                // show dialog
                showConfirmAlertDialog()
            }

        }
        startHandler()
    }

    private fun showConfirmAlertDialog() {
        val customView: View = layoutInflater.inflate(R.layout.session_expired_alert, null)
        val confirmButton = customView.findViewById<Button>(R.id.btnProceed)
        val cancelButton = customView.findViewById<Button>(R.id.btnCancel)


        val builder = AlertDialog.Builder(this)
        builder.setView(customView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        cancelButton.setOnClickListener {
            dialog.dismiss()
            //super.onBackPressed()
            finishAffinity();
            System.exit(0)
        }

        confirmButton.setOnClickListener {
            prefManager?.apiKey = ""
            dialog.dismiss()
            val intent: Intent = Intent(this, ActivityLogin::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        stopHandler()
        startHandler()
    }

    open fun startHandler() {

        handler!!.postDelayed(runnable!!, 3 * 60 * 1000); // 3 mins of inactivity for prod.
//        handler!!.postDelayed(runnable!!, 59 * 60 * 1000); // 60 mins of inactivity for dev.
//        handler!!.postDelayed(runnable!!,  30 * 1000); // 30 secs of inactivity for test.

    }

    open fun stopHandler() {

        handler!!.removeCallbacks(runnable!!)

        //  Toast.makeText(applicationContext, "User is active", Toast.LENGTH_SHORT).show()
        Log.i("device_activity", "Device Active")
    }
}