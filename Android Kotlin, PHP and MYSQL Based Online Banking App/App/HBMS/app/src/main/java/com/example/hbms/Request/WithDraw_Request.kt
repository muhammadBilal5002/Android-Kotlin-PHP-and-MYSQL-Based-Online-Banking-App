package com.example.hbms.Request

data class WithDraw_Request(
    val action:String="WITHDRAW_REQUEST",
    val userEmail:String="",
    val fmEmail:String="",
    val requestAmount:Double=0.0,
    val userOTP:String="",

)
