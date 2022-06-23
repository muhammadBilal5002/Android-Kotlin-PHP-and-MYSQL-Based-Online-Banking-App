package com.example.hbms.Response

import com.example.hbms.utilities.Transaction

data class TransactionHistory_Respond (
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var transactions:MutableList<Transaction> = mutableListOf(),
)