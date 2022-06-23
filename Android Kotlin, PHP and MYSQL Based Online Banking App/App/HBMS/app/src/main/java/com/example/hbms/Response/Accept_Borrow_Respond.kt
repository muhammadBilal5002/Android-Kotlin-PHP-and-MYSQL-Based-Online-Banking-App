package com.example.hbms.Response

import com.example.hbms.utilities.Request

data class Accept_Borrow_Respond(
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "" ,
    var allRequests:MutableList<Request> = mutableListOf() ,
    var pendingRequests:MutableList<Request> = mutableListOf()
)
