package com.example.hbms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.RespondRequest_Request
import com.example.hbms.Response.RespondRequest_Respond
import kotlinx.android.synthetic.main.activity_make_transaction.*
import kotlinx.android.synthetic.main.activity_respond_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Respond_Request : AppCompatActivity(), Callback<RespondRequest_Respond> {
    var LD = LoadingDialog(this)
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_respond_request)
        Requester_Email.text = "Requester Email: " + Constrain.cur_req.requesterEmail
        Request_Amount.text = "Request Amount: " + Constrain.cur_req.requestAmount.toString()
        Request_Message.text = "Request Message: " + Constrain.cur_req.requestMessage
        Responded_Amount.setText(Constrain.cur_req.requestAmount.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    fun send_respond(view: View) {
        var message = Respond_Message.text.toString().trim()
        var amount = Responded_Amount.text.toString().trim()
        var flag = validate(message,amount)
        if(flag){
            var req = RespondRequest_Request(requestId=Constrain.cur_req.requestId,respondMessage=message,respondAmount=amount.toDouble())
            var res = request_contract.RespondRequest_request(req)
            res.enqueue(this)
            LD.startLoading()
        }
        else{
            Toast.makeText(this,"Invalid Value(s)", Toast.LENGTH_SHORT).show()
        }
    }
    fun validate(message:String,amount:String):Boolean{
        if (!message.isNotEmpty()){
            try {
                amount.toDouble()
            }
            catch (e:Exception){
                return false
            }
            return false
        }
        return true
    }

    override fun onResponse(call: Call<RespondRequest_Respond>, response: Response<RespondRequest_Respond>) {
        LD.isDismiss()
     var resp = response.body()
        if(resp!=null){
            if(resp.status){
                Toast.makeText(this,"Respond Successfully Sent",Toast.LENGTH_SHORT).show()
                LocalDataProvider(this).saveData("balance",resp.balance.toString())
                onBackPressed()
            }
            else{
                Toast.makeText(this,"${resp.message}",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Server Side Problem",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<RespondRequest_Respond>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"fail${t.message}",Toast.LENGTH_LONG).show()
    }

}