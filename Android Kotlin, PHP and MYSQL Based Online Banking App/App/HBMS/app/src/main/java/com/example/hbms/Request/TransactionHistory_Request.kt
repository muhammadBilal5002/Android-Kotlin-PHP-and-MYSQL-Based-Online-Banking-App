package com.example.hbms.Request

data class TransactionHistory_Request (
    var action: String = "GET_TRANSACTIONS",
    var userEmail: String ="",
)