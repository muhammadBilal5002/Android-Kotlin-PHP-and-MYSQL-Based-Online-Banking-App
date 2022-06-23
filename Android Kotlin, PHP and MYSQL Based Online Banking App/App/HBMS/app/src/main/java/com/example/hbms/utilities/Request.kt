package com.example.hbms.utilities

data class Request(
    var requestId: Int =-1,
    var requestAmount: Double =0.0,
    var requestDate: String ="",
    var requestMessage: String ="",
    var requesterEmail: String ="",
    var requestStatus: String = "",
    var respondMessage: String = "",
    var respondAmount: String = "",
    var respondDate: String = "",
)
