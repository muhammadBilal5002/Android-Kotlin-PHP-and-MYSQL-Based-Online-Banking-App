package com.example.hbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.Transaction_Request
import com.example.hbms.Response.Transaction_Respond
import kotlinx.android.synthetic.main.activity_add_balance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Add_Balance : AppCompatActivity() , Callback<Transaction_Respond> {
    var LD = LoadingDialog(this)
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_balance)
    }

    fun Add(view: View) {
        var email = user_email.text.toString().trim()
        var amount = Amount.text.toString().trim()
        var purpose = "@Add Balance To User"
        var flag = validate(email,amount)
        if(flag){
            var req = Transaction_Request(action = "ADD_BALANCE", transactionAmount=amount.toDouble(),transactionPurpose=purpose,receiverEmail=email,senderEmail=
            LocalDataProvider(this).getData("email"))
            var res = request_contract.transaction_request(req)
            res.enqueue(this)
            LD.startLoading()
        }
        else{
            Toast.makeText(this,"Invalid Value(s)", Toast.LENGTH_SHORT).show()
        }

    }
    fun validate(email:String,amount:String):Boolean{
        val emailRegex = Regex("^[\\w!#\$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$")
        if (!emailRegex.matches(email) ){
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

    override fun onResponse(call: Call<Transaction_Respond>, response: Response<Transaction_Respond>) {
        LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                Toast.makeText(this,"Balance Added Successfully",Toast.LENGTH_SHORT).show()
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

    override fun onFailure(call: Call<Transaction_Respond>, t: Throwable) {
        LD.isDismiss()
        Toast.makeText(this,"${t.message}",Toast.LENGTH_SHORT).show()
    }
}