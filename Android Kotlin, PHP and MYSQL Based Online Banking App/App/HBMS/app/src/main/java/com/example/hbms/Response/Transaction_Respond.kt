package com.example.hbms.Response

data class Transaction_Respond (
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var balance:Double = 0.0,
    var withDraw:Double = 0.0
)