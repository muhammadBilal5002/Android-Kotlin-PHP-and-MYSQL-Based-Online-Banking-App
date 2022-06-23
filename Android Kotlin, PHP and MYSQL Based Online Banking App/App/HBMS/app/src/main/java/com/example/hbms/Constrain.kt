package com.example.hbms

import com.example.hbms.utilities.Request

object Constrain {
    var baseline = "http://192.168.239.21/HBMS/"
    var pendinglist:MutableList<Request> = mutableListOf()
    var alllist:MutableList<Request> = mutableListOf()
    var cur_req:Request = Request()
}