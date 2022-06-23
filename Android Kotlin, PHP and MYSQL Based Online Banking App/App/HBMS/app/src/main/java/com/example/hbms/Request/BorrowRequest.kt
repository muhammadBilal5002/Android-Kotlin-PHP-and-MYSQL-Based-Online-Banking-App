package com.example.hbms.Request

data class Borrow_Request (
    var action: String = "SEND_REQUEST",
    var requestMessage: String ="",
    var requestAmount: Double =0.0,
    var requesterEmail: String ="",
    var responderEmail: String=""

)