package com.example.hbms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.hbms.Request.IRRequest
import com.example.hbms.Request.WithDraw_Request
import com.example.hbms.Response.WithDraw_Respond
import kotlinx.android.synthetic.main.activity_with_draw_balance.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WithDraw_Balance : AppCompatActivity(), Callback<WithDraw_Respond> {
    var flag2 = true
    var LD = LoadingDialog(this)
    val client = NetworkClient.getnetworkobject()
    val request_contract = client.create(IRRequest::class.java)
    lateinit var localDataProvider: LocalDataProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_with_draw_balance)
        localDataProvider = LocalDataProvider(this)

    }

    fun Send_OTP(view: View) {
        var email = reciver_email.text.toString().trim()
        var amount = Amount.text.toString().trim()
        var flag = validate(email,amount)
        if(flag){
        var req = WithDraw_Request(userEmail=email,fmEmail=localDataProvider.getData("email"),requestAmount=amount.toDouble())
        flag2=true
        localDataProvider.saveData("amounttobewithdraw",amount)
        localDataProvider.saveData("emailfromwithdraw",email)
        var res = request_contract.WithDraw_request(req)
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

    fun WithDraw(view: View) {
        var req = WithDraw_Request(action = "WITHDRAW_RESPONSE", userEmail=localDataProvider.getData("emailfromwithdraw"),fmEmail=localDataProvider.getData("email"),requestAmount=localDataProvider.getData("amounttobewithdraw").toDouble(), userOTP = OTP.text.toString().trim())
        var res = request_contract.WithDraw_request(req)
        flag2=false
        res.enqueue(this)
        LD.startLoading()
    }


    override fun onResponse(call: Call<WithDraw_Respond>, response: Response<WithDraw_Respond>) {
        LD.isDismiss()
        var resp = response.body()
        if(resp!=null){
            if(resp.status){
                if(flag2){
                    Toast.makeText(this,"${resp.message}",Toast.LENGTH_SHORT).show()
                    button5.isEnabled = true
                }
                else{
                    localDataProvider.saveData("amounttobewithdraw","")
                    localDataProvider.saveData("emailfromwithdraw","")
                    localDataProvider.saveData("withdraw",resp.withDraw.toString())
                    Toast.makeText(this,"${resp.message}",Toast.LENGTH_SHORT).show()
                    button5.isEnabled = false
                    onBackPressed()
                }
            }
            else{
                Toast.makeText(this,"${resp.message}",Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this,"Server Side Error",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(call: Call<WithDraw_Respond>, t: Throwable) {
        Toast.makeText(this,"${t.message}",Toast.LENGTH_SHORT).show()
    }
}