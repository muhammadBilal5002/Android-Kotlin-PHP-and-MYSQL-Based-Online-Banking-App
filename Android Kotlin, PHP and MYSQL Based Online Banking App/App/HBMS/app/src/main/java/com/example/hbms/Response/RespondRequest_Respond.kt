package com.example.hbms.Response

import com.example.hbms.utilities.Request

data class RespondRequest_Respond(
    var status:Boolean = false,
    var respondeCode:Int = -1,
    var message:String = "",
    var balance:Double = 0.0,

)