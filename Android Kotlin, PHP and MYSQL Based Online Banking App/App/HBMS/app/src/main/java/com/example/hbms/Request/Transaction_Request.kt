package com.example.hbms.Request

data class Transaction_Request (
    var action: String = "DO_TRANSACTION",
    var transactionAmount: Double = 0.0,
    var transactionPurpose: String ="",
    var senderEmail: String ="",
    var receiverEmail: String=""

)