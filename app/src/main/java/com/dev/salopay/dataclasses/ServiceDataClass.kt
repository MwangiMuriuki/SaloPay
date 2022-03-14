package com.dev.salopay.dataclasses

data class ServiceDataClass(
    var success: Boolean = false,
    var message: String? = null,
    var data: ServiceData? = null
) {}

data class ServiceData(
    var previous_transaction: String? = null,
    var service: Service? = null,
    var transaction_charges: List<TransactionCharges> = ArrayList<TransactionCharges>()
){
    data class Service(
        var id: Int? = 0,
        var is_closed: Int? = 0,
        var is_active: Int? = 0,
        var service_type: String? = null,
        var name: String? = null,
        var status: String? = null,
        var client: ServiceClient? = null,
        var has_active_cycle: Boolean = false,
        var active_cycle: ServiceActiveCycle? = null
    ){
        data class ServiceClient(
            var id: Int? = 0,
            var name: String? = null
        ){}
        data class ServiceActiveCycle(
            var id: Int? = 0,
            var rate: Int? = 0,
            var cycle_start_date: String? = null,
            var cycle_end_date: String? = null,
            var month: Int? = 0,
            var year: Int? = 0,
            var is_closed: Int? = 0,
            ){}
    }

    data class TransactionCharges(
        var from: Int = 0,
        var to: Int = 0,
        var charges: Int = 0
    ){}
}

