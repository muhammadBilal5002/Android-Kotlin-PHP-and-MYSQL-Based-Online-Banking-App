package com.example.hbms.Request

data class RespondRequest_Request(
        var action: String = "RESPOND_REQUEST",
        var requestId: Int =-1,
        var respondMessage: String ="",
        var respondAmount: Double =0.0
)