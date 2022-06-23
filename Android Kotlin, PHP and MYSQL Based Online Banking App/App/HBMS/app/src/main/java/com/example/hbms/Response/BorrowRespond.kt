package com.example.hbms.Response

data class BorrowRespond (
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var phone:String = "" ,
)