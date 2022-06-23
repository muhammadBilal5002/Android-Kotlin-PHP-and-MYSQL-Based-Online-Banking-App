package com.example.hbms.Request

data class Login_Request (
    var action: String = "LOGIN_USER",
    var userEmail: String ="",
    var userPassword: String=""

)
