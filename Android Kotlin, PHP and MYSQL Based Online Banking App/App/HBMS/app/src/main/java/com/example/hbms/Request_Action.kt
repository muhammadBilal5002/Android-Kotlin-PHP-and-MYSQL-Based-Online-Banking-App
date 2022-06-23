package com.example.hbms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hbms.Request.Accept_Borrow_Request
import com.example.hbms.Request.IRRequest
import com.example.hbms.Response.Accept_Borrow_Respond
import com.example.hbms.adapter.adapter2
import com.example.hbms.adapter.adapter3
import kotlinx.android.synthetic.main.activity_request_action.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Request_Action : AppCompatActivity(), Callback<Accept_Borrow_Respond> {
    var my = this;
    var LD = LoadingDialog(this)
    private lateinit var adp2: adapter2
    private lateinit var adp3: adapter3
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_action)

    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onStart() {
        super.onStart()
        request_rec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        var req = Accept_Borrow_Request(userEmail=LocalDataProvider(this).getData("email"))
        var res = request_contract.requestlist_request(req)
        res.enqueue(this)
        LD.startLoading()
    }
    override fun onResponse(call: Call<Accept_Borrow_Respond>, response: Response<Accept_Borrow_Respond>) {
        var resp = response.body()
        LD.isDismiss()
        if(resp!=null){
            if(resp.status){
            text.isVisible = false
            request_rec.isVisible = true
            Constrain.pendinglist = resp.pendingRequests
            Constrain.alllist = resp.allRequests

            adp2 = adapter2(Constrain.pendinglist,this)
            adp2.setItemClickListner(object : adapter2.RespondAction{
                override fun action(position: Int) {
                    Constrain.cur_req = Constrain.pendinglist[position]
                    var intent = Intent(this@Request_Action,Respond_Request::class.java)
                    startActivity(intent)
                    finish()
                }

            })
            adp3 = adapter3(Constrain.alllist)

            if(Constrain.alllist.size!=0) {
                request_rec.adapter = adp3
            }
            else{
                request_rec.isVisible = false
                text.isVisible = true
                text.text = "Responded Request List Is Empty"
                }
            }
            }
    }

    override fun onFailure(call: Call<Accept_Borrow_Respond>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"Check Your Internet Connection \n${t.message}", Toast.LENGTH_SHORT).show()
    }

    fun Responded_Request(view: View) {
        if(Constrain.alllist.size!=0) {
            text.isVisible = false
            request_rec.isVisible = true
            request_rec.adapter = adp3

        }
        else{
            request_rec.isVisible = false
            text.isVisible = true
            text.text = "Responded Request List Is Empty"
        }
    }
    fun Pending_Request(view: View) {

        if(Constrain.pendinglist.size!=0) {
            text.isVisible = false
            request_rec.isVisible = true
            request_rec.adapter = adp2

        }
        else{
            request_rec.isVisible = false
            text.isVisible = true
            text.text = "Pending Request List Is Empty"
        }

    }
}