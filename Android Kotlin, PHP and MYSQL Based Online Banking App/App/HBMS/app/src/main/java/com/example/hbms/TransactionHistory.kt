package com.example.hbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.TransactionHistory_Request
import com.example.hbms.Response.TransactionHistory_Respond
import com.example.hbms.adapter.adapter1
import kotlinx.android.synthetic.main.activity_transaction_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionHistory : AppCompatActivity(), Callback<TransactionHistory_Respond> {
    var LD = LoadingDialog(this)
    private lateinit var adp1: adapter1
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        rec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        var req = TransactionHistory_Request(userEmail=LocalDataProvider(this).getData("email"))
        var res = request_contract.transactionhistory_request(req)
        res.enqueue(this)
        LD.startLoading()

    }

    override fun onResponse(call: Call<TransactionHistory_Respond>, response: Response<TransactionHistory_Respond>){
        var resp = response.body()
        LD.isDismiss()
        if(resp!=null){
            adp1 = adapter1(resp.transactions)
            rec.adapter = adp1
        }
    }

    override fun onFailure(call: Call<TransactionHistory_Respond>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"Check Your Internet Connection", Toast.LENGTH_SHORT).show()
    }

}


