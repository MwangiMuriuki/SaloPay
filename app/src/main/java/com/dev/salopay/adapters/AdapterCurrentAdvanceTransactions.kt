package com.dev.salopay.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dev.salopay.R
import com.dev.salopay.dataclasses.CurrentAdvanceTransactionsDataClass

class AdapterCurrentAdvanceTransactions(val context: Context, val transactionDataList: List<CurrentAdvanceTransactionsDataClass.TransactionData.TransactionsList?>)
    : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        return MyViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataClass: CurrentAdvanceTransactionsDataClass.TransactionData.TransactionsList? = transactionDataList[position]

        val interest = dataClass?.interest
        val charges = dataClass?.charges
        val deductions = interest?.toBigDecimal()?.plus(charges?.toBigDecimal()!!)

        holder.advanceRequested?.text = dataClass?.total_amount.toString()
        holder.totalDeductions?.text = deductions.toString()
        holder.amountReceived?.text = dataClass?.amount_received.toString()
        holder.dateView?.text = dataClass?.transaction_date
        holder.itemLayout!!.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return transactionDataList.size
    }
}

class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.advance_transaction_layout, parent, false)) {

    var advanceRequested: TextView? = null
    var totalDeductions: TextView? = null
    var amountReceived: TextView? = null
    var dateView: TextView? = null
    var itemLayout: ConstraintLayout? = null

    init{
        advanceRequested = itemView.findViewById(R.id.advanceRequested)
        totalDeductions = itemView.findViewById(R.id.totalDeductions)
        amountReceived = itemView.findViewById(R.id.amountReceived)
        dateView = itemView.findViewById(R.id.dateView)
        itemLayout = itemView.findViewById(R.id.itemLayout)
    }
}
