package com.example.hbms.Response

data class Login_Response (
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var userName:String = "",
    var userEmail:String = "",
    var userBalance:String = "",
    var userPhoneno:String = "",
    var userOTP:String = "",
    var userWithDraw:String = ""
)
