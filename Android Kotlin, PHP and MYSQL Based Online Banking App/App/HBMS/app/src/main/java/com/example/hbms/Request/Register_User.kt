package com.example.hbms.Request


data class Register_User_Request(
    var action:String = "REGISTER_USER",
    var userName:String = "",
    var userEmail:String = "",
    var userPassword:String = "",
    var userPhoneno:String = ""
)
