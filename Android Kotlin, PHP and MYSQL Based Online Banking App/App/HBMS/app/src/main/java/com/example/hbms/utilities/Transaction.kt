package com.example.hbms.utilities

data class Transaction(
    var transactionId: Int =-1,
    var transactionAmount: Double =0.0,
    var transactionDate: String ="",
    var transactionPurpose: String ="",
    var sender: String ="",
    var receiver: String ="",

)
