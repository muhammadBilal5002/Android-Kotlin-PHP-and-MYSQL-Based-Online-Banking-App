package com.example.hbms.Request

import com.example.hbms.Response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface IRRequest {
    //User
    @POST("service.php")
    fun myrequest(@Body request:Register_User_Request): Call<Register_User_Response>

    @POST("service.php")
    fun login_request(@Body request:Login_Request): Call<Login_Response>

    @POST("service.php")
    fun transaction_request(@Body request:Transaction_Request): Call<Transaction_Respond>

    @POST("service.php")
    fun borrow_request(@Body request:Borrow_Request): Call<BorrowRespond>

    @POST("service.php")
    fun transactionhistory_request(@Body request:TransactionHistory_Request): Call<TransactionHistory_Respond>

    @POST("service.php")
    fun requestlist_request(@Body request:Accept_Borrow_Request): Call<Accept_Borrow_Respond>

    @POST("service.php")
    fun RespondRequest_request(@Body request:RespondRequest_Request): Call<RespondRequest_Respond>

    @POST("service.php")
    fun WithDraw_request(@Body request:WithDraw_Request): Call<WithDraw_Respond>


}