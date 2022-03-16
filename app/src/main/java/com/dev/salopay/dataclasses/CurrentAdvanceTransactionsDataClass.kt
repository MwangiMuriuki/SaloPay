package com.dev.salopay.dataclasses

data class CurrentAdvanceTransactionsDataClass(
    var success: Boolean = true,
    var message: String? = null,
    var data: TransactionData? = null

) {
    data class TransactionData(
        var transactions: List<TransactionsList?>? = ArrayList<TransactionsList?>()
    ){
        data class TransactionsList(
            var id: Int? = 0,
            var cycle_id: Int? = 0,
            var description: String? = null,
            var total_amount: Int? = 0,
            var amount_received: Int? = 0,
            var interest: Int? = 0,
            var charges: Int? = 0,
            var transaction_date: String? = null,
            var is_received: Boolean? = false,
            var date_received: String? = null,
            var service_type: String? = null,
            var transaction_status: Boolean? = false,
            var ref_code: String? = null
        ){}
    }
}