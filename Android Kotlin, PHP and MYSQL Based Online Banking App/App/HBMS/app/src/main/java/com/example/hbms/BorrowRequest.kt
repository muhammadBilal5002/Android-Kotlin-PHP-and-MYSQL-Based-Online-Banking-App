package com.example.hbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.Borrow_Request
import com.example.hbms.Request.IRRequest
import com.example.hbms.Response.BorrowRespond
import kotlinx.android.synthetic.main.activity_borrow_request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BorrowRequest : AppCompatActivity() , Callback<BorrowRespond> {
    var LD = LoadingDialog(this)
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow_request)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    fun Send_Request(view: View) {
        var email = responder_email.text.toString().trim()
        var amount = Amount.text.toString().trim()
        var message = message.text.toString().trim()
        var flag = validate(email,amount,message)
        if(flag){
            var req = Borrow_Request(requestAmount=amount.toDouble(),requestMessage=message,responderEmail=email,requesterEmail=
            LocalDataProvider(this).getData("email"))
            var res = request_contract.borrow_request(req)
            res.enqueue(this)
            LD.startLoading()
        }
        else{
            Toast.makeText(this,"Invalid Value(s)", Toast.LENGTH_SHORT).show()
        }
    }

    fun validate(email:String,amount:String,message:String):Boolean{
        val emailRegex = Regex("^[\\w!#\$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        if (!message.isNotEmpty() || !emailRegex.matches(email) ){
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
    override fun onResponse(call: Call<BorrowRespond>, response: Response<BorrowRespond>) {
       LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                Toast.makeText(this,"Request Sent",Toast.LENGTH_SHORT).show()
                onBackPressed()
                finish()
            }
            else{
                Toast.makeText(this,"${resp.message}",Toast.LENGTH_SHORT).show()
            }
        }
        else{

            Toast.makeText(this,"Server Side Problem",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<BorrowRespond>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show()
    }
}