package com.example.hbms.Response

data class WithDraw_Respond(
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var withDraw:Double = 0.0,
)
